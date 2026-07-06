import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Refugio } from '../models/refugio';

export interface ApiCard {
  id?: number;
  titulo?: string;
  title?: string;
  nombre?: string;
  descripcion?: string;
  description?: string;
  contenido?: string;
  texto?: string;
  categoria?: string;
  tipoDesastre?: string;
  ordenVisualizacion?: number;
  icono?: string;
  icon?: string;
}

export interface Kit {
  id?: number;
  idKit?: number;
  nombre?: string;
}

export interface ItemKit {
  id?: number;
  nombre?: string;
  nombreItem?: string;
  titulo?: string;
  descripcion?: string;
  categoria?: string;
  cantidad?: string | number;
  cantidadRecomendada?: string | number;
  estado?: string;
  completo?: boolean;
  completado?: boolean;
  tieneItem?: boolean;
  icono?: string;
  icon?: string;
}

export interface Faq {
  id?: number;
  pregunta?: string;
  question?: string;
  respuesta?: string;
  answer?: string;
}

export interface CrearPuntoSeguroRequest {
  nombre: string;
  tipo?: string;
  direccion?: string;
  referencia?: string;
  referenciaSeguridad?: string;
  porQueEsSeguro?: string;
  latitud: number;
  longitud: number;
  capacidad?: number;
}

@Injectable({
  providedIn: 'root',
})
export class Safelink {
  private http = inject(HttpClient);
  private readonly base = environment.apiUrl;

  listarPuntosSeguros(): Observable<Refugio[]> {
    return this.http.get<unknown>(`${this.base}/puntos-seguros`).pipe(
      map(response => this.extractArray<Refugio>(response))
    );
  }

  listarPuntosSegurosUsuarioMapa(idUsuario: number): Observable<Refugio[]> {
    return this.http.get<unknown>(`${this.base}/puntos-seguros/usuario/${idUsuario}/mapa`).pipe(
      map(response => this.extractArray<Refugio>(response))
    );
  }

  crearPuntoSeguroUsuario(idUsuario: number, punto: CrearPuntoSeguroRequest): Observable<Refugio> {
    return this.http.post<unknown>(`${this.base}/puntos-seguros/usuario/${idUsuario}`, punto).pipe(
      map(response => this.extractArray<Refugio>(response)[0] ?? response as Refugio)
    );
  }

  eliminarPuntoSeguroUsuario(idUsuario: number, idPunto: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/puntos-seguros/usuario/${idUsuario}/${idPunto}`);
  }

  listarRefugiosDisponibles(): Observable<Refugio[]> {
    return this.http.get<unknown>(`${this.base}/refugios/disponible`).pipe(
      map(response => this.extractArray<Refugio>(response))
    );
  }

  listarGuias(): Observable<ApiCard[]> {
    return this.http.get<ApiCard[]>(`${this.base}/educacion/guias`);
  }

  listarGuiasOffline(): Observable<ApiCard[]> {
    return this.http.get<ApiCard[]>(`${this.base}/educacion/guias/offline`);
  }

  filtrarGuias(params: Record<string, string | number | boolean>): Observable<ApiCard[]> {
    let httpParams = new HttpParams();
    Object.entries(params).forEach(([key, value]) => {
      httpParams = httpParams.set(key, String(value));
    });
    return this.http.get<ApiCard[]>(`${this.base}/educacion/guias/filtrar`, { params: httpParams });
  }

  listarConsejos(): Observable<ApiCard[]> {
    return this.http.get<unknown>(`${this.base}/educacion/consejos`).pipe(
      map(response => this.extractArray<ApiCard>(response))
    );
  }

  listarFaq(): Observable<Faq[]> {
    return this.http.get<Faq[]>(`${this.base}/soporte/faq`);
  }

  listarKitsPorUsuario(idUsuario: number): Observable<Kit[]> {
    return this.http.get<unknown>(`${this.base}/kits/usuario/${idUsuario}`).pipe(
      map(response => this.extractArray<Kit>(response))
    );
  }

  listarItemsKit(idKit: number): Observable<ItemKit[]> {
    return this.http.get<unknown>(`${this.base}/items-kit/kit/${idKit}`).pipe(
      map(response => this.extractArray<ItemKit>(response))
    );
  }

  listarItemsKitRecomendados(): Observable<ItemKit[]> {
    return this.http.get<unknown>(`${this.base}/items-kit/recomendados`).pipe(
      map(response => this.extractArray<ItemKit>(response))
    );
  }

  private extractArray<T>(response: unknown): T[] {
    if (Array.isArray(response)) {
      return response as T[];
    }

    if (!this.isRecord(response)) {
      return [];
    }

    const arrayKeys = ['data', 'content', 'items', 'itemsKit', 'kits', 'result', 'resultado', 'consejos', 'puntos', 'refugios'];
    let foundEmptyArray = false;
    for (const key of arrayKeys) {
      const value = response[key];
      if (value === undefined || value === null) {
        continue;
      }

      if (Array.isArray(value)) {
        if (value.length) {
          return value as T[];
        }
        foundEmptyArray = true;
        continue;
      }

      if (this.isRecord(value)) {
        const extracted = this.extractArray<T>(value);
        if (extracted.length || this.hasEntityData(value)) {
          return extracted;
        }
      }
    }

    if (foundEmptyArray) {
      return [];
    }

    if (this.hasEntityData(response)) {
      return [response as T];
    }

    return [];
  }

  private hasEntityData(response: Record<string, unknown>): boolean {
    return ['id', 'idKit', 'nombre', 'nombreItem', 'titulo', 'descripcion', 'contenido', 'latitud', 'longitud']
      .some(key => response[key] !== undefined && response[key] !== null);
  }

  private isRecord(value: unknown): value is Record<string, unknown> {
    return typeof value === 'object' && value !== null;
  }
}
