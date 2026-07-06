import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map, Observable, of, switchMap } from 'rxjs';
import { Usuario } from '../models/usuario';
import { Familiar } from '../models/Familiar';
import { environment } from '../../environments/environment';

export interface FamiliaAdmin {
  id?: number;
  nombre?: string;
  idUsuario?: number;
  usuarioId?: number;
  usuarioNombre?: string;
  usuarioCorreo?: string;
  familiares: Familiar[];
}

@Injectable({
  providedIn: 'root',
})
export class Admin {
  private http = inject(HttpClient);
  private readonly BASE = environment.apiUrl;

  listarUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.BASE}/usuarios`);
  }

  eliminarUsuario(idUsuario: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE}/usuarios/${idUsuario}`);
  }

  listarFamiliasPorUsuario(idUsuario: number): Observable<Familiar[]> {
    return this.http.get<unknown>(`${this.BASE}/familias/usuario/${idUsuario}`).pipe(
      map(response => this.normalizarFamilias(response).flatMap(familia => familia.familiares))
    );
  }

  listarFamilias(): Observable<FamiliaAdmin[]> {
    return this.http.get<unknown>(`${this.BASE}/familias`).pipe(
      map(response => this.normalizarFamilias(response))
    );
  }

  listarFamiliasDeUsuarios(): Observable<Familiar[]> {
    return this.listarUsuarios().pipe(
      switchMap(usuarios => {
        if (!usuarios.length) {
          return of([]);
        }

        return forkJoin(usuarios.map(usuario => this.listarFamiliasPorUsuario(usuario.id))).pipe(
          map(familiasPorUsuario => familiasPorUsuario.flat())
        );
      })
    );
  }

  private normalizarFamilias(response: unknown): FamiliaAdmin[] {
    return this.extraerFamilias(response).map((item, index) => this.normalizarFamilia(item, index));
  }

  private extraerFamilias(response: unknown): unknown[] {
    if (Array.isArray(response)) {
      return response;
    }

    if (!this.isRecord(response)) {
      return [];
    }

    const keys = ['familias', 'data', 'content', 'items', 'result', 'resultado'];
    for (const key of keys) {
      const extracted = this.extraerFamilias(response[key]);
      if (extracted.length) {
        return extracted;
      }
    }

    if (Array.isArray(response['familiares']) || this.tieneDatosDeFamiliar(response)) {
      return [response];
    }

    return [];
  }

  private normalizarFamilia(item: unknown, index: number): FamiliaAdmin {
    if (!this.isRecord(item)) {
      return { nombre: `Familia ${index + 1}`, familiares: [] };
    }

    const usuario = this.isRecord(item['usuario']) ? item['usuario'] : undefined;
    const familiaresRaw = Array.isArray(item['familiares']) ? item['familiares'] : [];
    const familiares = (familiaresRaw.length ? familiaresRaw : this.tieneDatosDeFamiliar(item) ? [item] : [])
      .map(familiar => this.normalizarFamiliar(familiar))
      .filter((familiar): familiar is Familiar => familiar !== null);

    const idUsuario = this.numberValue(item, 'idUsuario')
      ?? this.numberValue(item, 'usuarioId')
      ?? (usuario ? this.numberValue(usuario, 'id') : undefined)
      ?? (usuario ? this.numberValue(usuario, 'idUsuario') : undefined);

    return {
      id: this.numberValue(item, 'id') ?? this.numberValue(item, 'idFamilia'),
      nombre: this.stringValue(item, 'nombre') ?? this.stringValue(item, 'nombreFamilia') ?? `Familia ${index + 1}`,
      idUsuario,
      usuarioId: idUsuario,
      usuarioNombre: usuario ? this.nombreUsuario(usuario) : this.stringValue(item, 'usuarioNombre'),
      usuarioCorreo: usuario ? this.stringValue(usuario, 'correo') ?? this.stringValue(usuario, 'email') : this.stringValue(item, 'usuarioCorreo'),
      familiares,
    };
  }

  private normalizarFamiliar(item: unknown): Familiar | null {
    if (!this.isRecord(item)) {
      return null;
    }

    const nombre = this.stringValue(item, 'nombre') ?? this.stringValue(item, 'name') ?? '';
    const relacion = this.stringValue(item, 'relacion') ?? this.stringValue(item, 'parentesco') ?? 'Familiar';

    if (!nombre && !this.tieneDatosDeFamiliar(item)) {
      return null;
    }

    return {
      id: this.numberValue(item, 'id') ?? this.numberValue(item, 'idFamiliar'),
      nombre,
      telefono: this.stringValue(item, 'telefono') ?? this.stringValue(item, 'phone'),
      relacion,
      ubicacionHabitual: this.stringValue(item, 'ubicacionHabitual') ?? this.stringValue(item, 'ubicacion'),
      ubicacion: this.stringValue(item, 'ubicacion') ?? this.stringValue(item, 'ubicacionHabitual'),
      estado: this.stringValue(item, 'estado') ?? this.stringValue(item, 'status'),
      idUsuario: this.numberValue(item, 'idUsuario') ?? this.numberValue(item, 'usuarioId'),
      usuarioId: this.numberValue(item, 'usuarioId') ?? this.numberValue(item, 'idUsuario'),
    };
  }

  private nombreUsuario(usuario: Record<string, unknown>): string | undefined {
    const nombres = [
      this.stringValue(usuario, 'nombre'),
      this.stringValue(usuario, 'apellido'),
    ].filter((parte): parte is string => !!parte);

    return nombres.join(' ') || this.stringValue(usuario, 'correo') || this.stringValue(usuario, 'email');
  }

  private tieneDatosDeFamiliar(item: Record<string, unknown>): boolean {
    return ['nombre', 'telefono', 'relacion', 'parentesco', 'ubicacionHabitual', 'ubicacion', 'estado']
      .some(key => item[key] !== undefined && item[key] !== null);
  }

  private stringValue(item: Record<string, unknown>, key: string): string | undefined {
    const value = item[key];
    return typeof value === 'string' && value.trim() ? value : undefined;
  }

  private numberValue(item: Record<string, unknown>, key: string): number | undefined {
    const value = item[key];
    const parsed = typeof value === 'number' ? value : Number(value);
    return Number.isFinite(parsed) ? parsed : undefined;
  }

  private isRecord(value: unknown): value is Record<string, unknown> {
    return typeof value === 'object' && value !== null;
  }
}
