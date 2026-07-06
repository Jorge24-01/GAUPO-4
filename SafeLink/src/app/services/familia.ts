import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Familiar } from '../models/Familiar';
import { environment } from '../../environments/environment';

export interface RegistrarFamiliarRequest {
  nombre: string;
  telefono: string;
  relacion: string;
  ubicacionHabitual: string;
}

@Injectable({
  providedIn: 'root',})
export class Familia {
  private http = inject(HttpClient);
  private base = `${environment.apiUrl}/familias`;

  listarPorUsuario(id: number): Observable<Familiar[]> {
    return this.http.get<unknown>(`${this.base}/usuario/${id}`).pipe(
      map(response => this.normalizarFamiliares(response))
    );
  }

  registrarFamiliar(idUsuario: number, familiar: RegistrarFamiliarRequest): Observable<unknown> {
    return this.http.post<unknown>(`${this.base}/usuario/${idUsuario}/familiares`, familiar);
  }

  private normalizarFamiliares(response: unknown): Familiar[] {
    const items = this.extraerArreglo(response);

    if (items.some(item => this.isRecord(item) && Array.isArray(item['familiares']))) {
      return items
        .flatMap(item => this.isRecord(item) ? this.extraerArreglo(item['familiares']) : [])
        .map(item => this.normalizarFamiliar(item))
        .filter((item): item is Familiar => item !== null);
    }

    return items
      .map(item => this.normalizarFamiliar(item))
      .filter((item): item is Familiar => item !== null);
  }

  private extraerArreglo(response: unknown): unknown[] {
    if (Array.isArray(response)) {
      return response;
    }

    if (!this.isRecord(response)) {
      return [];
    }

    const keys = ['familiares', 'familias', 'data', 'content', 'items', 'result', 'resultado'];
    for (const key of keys) {
      const extracted = this.extraerArreglo(response[key]);
      if (extracted.length) {
        return extracted;
      }
    }

    if (this.tieneDatosDeFamiliar(response)) {
      return [response];
    }

    return [];
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
