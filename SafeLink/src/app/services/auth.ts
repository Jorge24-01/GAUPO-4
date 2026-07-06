import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, Observable, of, tap } from 'rxjs';
import { AuthResponse, LoginRequest, RegisterRequest, Usuario } from '../models/usuario';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private http   = inject(HttpClient);
  private router = inject(Router);
  private readonly BASE = environment.apiUrl;
  static readonly TOKEN_KEY = 'safelink_token';
  static readonly USER_ID_KEY = 'safelink_id_usuario';
  static readonly USER_KEY = 'safelink_usuario';
  static readonly TIPO_USUARIO_KEY = 'tipoUsuario';
  static readonly NOMBRE_KEY = 'nombre';
  static readonly APELLIDO_KEY = 'apellido';
  static readonly CORREO_KEY = 'correo';

  isLoggedIn = signal<boolean>(false);
  currentUser = signal<Usuario | null>(null);

  constructor() {
    this.inicializarSesion();
  }

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.BASE}/usuarios/login`, req).pipe(
      tap(res => this.guardarSesion(res))
    );
  }

  registro(usuario: RegisterRequest): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.BASE}/usuarios/registro`, usuario);
  }

  logout(): void {
    localStorage.removeItem(Auth.TOKEN_KEY);
    localStorage.removeItem(Auth.USER_ID_KEY);
    localStorage.removeItem(Auth.USER_KEY);
    localStorage.removeItem(Auth.TIPO_USUARIO_KEY);
    localStorage.removeItem(Auth.NOMBRE_KEY);
    localStorage.removeItem(Auth.APELLIDO_KEY);
    localStorage.removeItem(Auth.CORREO_KEY);
    this.isLoggedIn.set(false);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(Auth.TOKEN_KEY);
  }

  getIdUsuario(): number | null {
    const id = this.toNumber(localStorage.getItem(Auth.USER_ID_KEY));
    if (id !== null) {
      return id;
    }

    const usuario = this.currentUser() ?? this.getStoredUser();
    return this.toNumber(usuario?.idUsuario ?? usuario?.usuarioId ?? usuario?.id);
  }

  isAdmin(): boolean {
    return this.usuarioActual()?.tipoUsuario?.toUpperCase() === 'ADMIN';
  }

  cargarUsuarioActual(force = false): Observable<Usuario | null> {
    const usuario = this.currentUser();

    if (!this.getToken()) {
      return of(null);
    }

    if (usuario && !force && this.usuarioTieneDatosSesion(usuario)) {
      return of(this.currentUser());
    }

    const idUsuario = this.getIdUsuario();
    if (!idUsuario) {
      return of(null);
    }

    return this.http.get<Usuario>(`${this.BASE}/usuarios/${idUsuario}`).pipe(
      tap(usuario => this.guardarUsuario(usuario)),
      catchError(() => of(null))
    );
  }

  nombreUsuario(): string {
    const usuario = this.usuarioActual();
    if (!usuario) {
      return 'Usuario';
    }

    return `${usuario.nombre ?? ''} ${usuario.apellido ?? ''}`.trim() || usuario.correo || usuario.email || 'Usuario';
  }

  tipoUsuario(): string {
    return this.usuarioActual()?.tipoUsuario ?? '';
  }

  inicialesUsuario(): string {
    return this.nombreUsuario()
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map(parte => parte.charAt(0).toUpperCase())
      .join('') || 'U';
  }

  private guardarSesion(res: AuthResponse): void {
    const token = res.token ?? res.jwt ?? res.accessToken;
    const usuarioBase = res.usuario ?? res.user;
    let usuario = usuarioBase
      ? {
        ...usuarioBase,
        apellido: usuarioBase.apellido ?? res.apellido ?? '',
        tipoUsuario: usuarioBase.tipoUsuario ?? res.tipoUsuario,
      }
      : this.usuarioDesdeRespuesta(res);
    const idUsuario = res.idUsuario ?? res.usuarioId ?? res.id ?? usuario?.id ?? usuario?.idUsuario ?? usuario?.usuarioId;
    const idUsuarioNumber = this.toNumber(idUsuario);

    if (usuario && idUsuarioNumber !== null) {
      usuario = {
        ...usuario,
        id: this.toNumber(usuario.id) ?? idUsuarioNumber,
        idUsuario: usuario.idUsuario ?? idUsuarioNumber,
      };
    }

    if (token) {
      localStorage.setItem(Auth.TOKEN_KEY, token);
    }

    if (idUsuarioNumber !== null) {
      localStorage.setItem(Auth.USER_ID_KEY, String(idUsuarioNumber));
    }

    if (usuario) {
      this.guardarUsuario(usuario);
    } else {
      this.cargarUsuarioActual().subscribe();
    }

    this.isLoggedIn.set(!!token);
  }

  private guardarUsuario(usuario: Usuario): void {
    const usuarioNormalizado = this.normalizarUsuario(usuario);
    localStorage.setItem(Auth.USER_KEY, JSON.stringify(usuarioNormalizado));
    this.guardarValor(Auth.USER_ID_KEY, usuarioNormalizado.idUsuario ?? usuarioNormalizado.usuarioId ?? usuarioNormalizado.id);
    this.guardarValor(Auth.TIPO_USUARIO_KEY, usuarioNormalizado.tipoUsuario);
    this.guardarValor(Auth.NOMBRE_KEY, usuarioNormalizado.nombre);
    this.guardarValor(Auth.APELLIDO_KEY, usuarioNormalizado.apellido);
    this.guardarValor(Auth.CORREO_KEY, usuarioNormalizado.correo ?? usuarioNormalizado.email);
    this.currentUser.set(usuarioNormalizado);
  }

  private usuarioDesdeRespuesta(res: AuthResponse): Usuario | undefined {
    if (!res.id && !res.idUsuario && !res.usuarioId && !res.nombre && !res.correo && !res.tipoUsuario) {
      return undefined;
    }

    return {
      id: res.id ?? res.idUsuario ?? res.usuarioId ?? 0,
      nombre: res.nombre ?? '',
      apellido: res.apellido ?? '',
      correo: res.correo,
      tipoUsuario: res.tipoUsuario,
    };
  }

  private usuarioActual(): Usuario | null {
    const usuario = this.currentUser();
    if (usuario) {
      return usuario;
    }

    const stored = this.getStoredUser();
    if (stored) {
      this.currentUser.set(stored);
    }

    return stored;
  }

  private inicializarSesion(): void {
    this.currentUser.set(this.getStoredUser());
    this.isLoggedIn.set(!!this.getToken());
  }

  private getStoredUser(): Usuario | null {
    const userFromJson = this.getStoredUserJson();
    if (userFromJson) {
      return userFromJson;
    }

    return this.getStoredUserFromKeys();
  }

  private getStoredUserJson(): Usuario | null {
    const raw = localStorage.getItem(Auth.USER_KEY);
    if (!raw) {
      return null;
    }

    try {
      return this.normalizarUsuario(JSON.parse(raw) as Usuario);
    } catch {
      localStorage.removeItem(Auth.USER_KEY);
      return null;
    }
  }

  private getStoredUserFromKeys(): Usuario | null {
    const id = this.toNumber(localStorage.getItem(Auth.USER_ID_KEY));
    const nombre = localStorage.getItem(Auth.NOMBRE_KEY) ?? '';
    const apellido = localStorage.getItem(Auth.APELLIDO_KEY) ?? '';
    const correo = localStorage.getItem(Auth.CORREO_KEY) ?? undefined;
    const tipoUsuario = localStorage.getItem(Auth.TIPO_USUARIO_KEY) ?? undefined;

    if (id === null && !nombre && !apellido && !correo && !tipoUsuario) {
      return null;
    }

    return {
      id: id ?? 0,
      idUsuario: id ?? undefined,
      nombre,
      apellido,
      correo,
      tipoUsuario,
    };
  }

  private normalizarUsuario(usuario: Usuario): Usuario {
    const id = this.toNumber(usuario.idUsuario ?? usuario.usuarioId ?? usuario.id);

    return {
      ...usuario,
      id: id ?? usuario.id ?? 0,
      idUsuario: usuario.idUsuario ?? id ?? undefined,
      nombre: usuario.nombre ?? '',
      apellido: usuario.apellido ?? '',
      correo: usuario.correo ?? usuario.email,
      tipoUsuario: usuario.tipoUsuario ?? usuario.rol ?? usuario.tipo,
    };
  }

  private usuarioTieneDatosSesion(usuario: Usuario): boolean {
    return !!usuario.tipoUsuario && !!(usuario.nombre || usuario.apellido || usuario.correo || usuario.email);
  }

  private guardarValor(key: string, value: string | number | undefined | null): void {
    if (value === undefined || value === null || value === '') {
      localStorage.removeItem(key);
      return;
    }

    localStorage.setItem(key, String(value));
  }

  private toNumber(value: string | number | undefined | null): number | null {
    if (value === undefined || value === null || value === '') {
      return null;
    }

    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : null;
  }

}
