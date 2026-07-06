export interface Refugio {
  id?: number;
  nombre: string;
  direccion?: string;
  referencia?: string;
  descripcion?: string;
  porQueEsSeguro?: string;
  tipo: string;
  latitud: number;
  longitud: number;
  capacidad?: number;
  activo?: boolean;
  disponible?: boolean;
}
