export interface Usuario {
  id: number;
  idUsuario?: number;
  usuarioId?: number;
  nombre: string;
  apellido: string;
  correo?: string;
  email?: string;
  telefono?: string;
  distrito?: string;
  rol?: string;
  tipo?: string;
  tipoUsuario?: string;
  password?: string;
  contrasena?: string;
}

export interface LoginRequest {
  correo: string;
  contrasena: string;
}

export interface AuthResponse {
  token?: string;
  jwt?: string;
  accessToken?: string;
  id?: number;
  usuarioId?: number;
  nombre?: string;
  apellido?: string;
  correo?: string;
  tipoUsuario?: string;
  idUsuario?: number;
  usuario?: Usuario;
  user?: Usuario;
}

export interface RegisterRequest {
  nombre: string;
  apellido: string;
  correo: string;
  telefono: string;
  distrito: string;
  contrasena: string;
  tipoUsuario: string;
}
