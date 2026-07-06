import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Refugio } from '../models/refugio';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',})
export class RefugioService {
   private http = inject(HttpClient);
  private base = `${environment.apiUrl}/refugios`;

  listar(): Observable<Refugio[]> {
    return this.http.get<Refugio[]>(this.base);
  }
  listarActivos(): Observable<Refugio[]> {
    return this.http.get<Refugio[]>(`${this.base}/disponible`);
  }
  getById(id: number): Observable<Refugio> {
    return this.http.get<Refugio>(`${this.base}/${id}`);
  }
}
