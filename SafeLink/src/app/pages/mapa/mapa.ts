import { ChangeDetectorRef, Component, OnInit, AfterViewInit, ElementRef, NgZone, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Observable, catchError, finalize, forkJoin, of } from 'rxjs';
import { TranslocoModule, TranslocoService, provideTranslocoScope } from '@jsverse/transloco';
import { Refugio } from '../../models/refugio';
import { Auth } from '../../services/auth';
import { CrearPuntoSeguroRequest, Safelink } from '../../services/safelink';

declare const google: any;
declare const L: any;

type UbicacionApi = Partial<Refugio> & {
  idPunto?: number | string;
  ubicacion?: string;
  descripcion?: string;
  referencia?: string;
  referenciaSeguridad?: string;
  puntoReferencia?: string;
  porqueSeguro?: string;
  porQueSeguro?: string;
  motivoSeguridad?: string;
  motivoSeguro?: string;
  razonSeguridad?: string;
  lat?: number | string;
  lng?: number | string;
  latitude?: number | string;
  longitude?: number | string;
  sugerido?: boolean;
  propio?: boolean;
};

type PuntoMapa = Refugio & {
  idPunto?: number;
  sugerido?: boolean;
  propio?: boolean;
};

type ModoRuta = 'walking' | 'driving' | 'bicycling';

interface NuevoPuntoForm {
  nombre: string;
  referenciaSeguridad: string;
}

interface PuntoPendiente {
  latitud: number;
  longitud: number;
  direccion: string;
}

@Component({
  selector: 'app-mapa',
  imports: [CommonModule, FormsModule, MatButtonModule, MatIconModule, MatSnackBarModule, TranslocoModule],
  providers: [provideTranslocoScope('mapa')],
  templateUrl: './mapa.html',
  styleUrl: './mapa.scss',
})
export class Mapa implements OnInit, AfterViewInit {
  @ViewChild('mapContainer', { static: false }) mapContainer?: ElementRef<HTMLDivElement>;

  private safelink = inject(Safelink);
  private auth = inject(Auth);
  private snack = inject(MatSnackBar);
  private cdr = inject(ChangeDetectorRef);
  private zone = inject(NgZone);
  private transloco = inject(TranslocoService);

  refugios: PuntoMapa[] = [];
  seleccionado: PuntoMapa | null = null;
  cargandoUbicaciones = true;
  modoAgregarPunto = false;
  puntoPendiente: PuntoPendiente | null = null;
  confirmandoPunto = false;
  mostrandoFormularioPunto = false;
  guardandoPunto = false;
  eliminandoPunto = false;
  resolviendoDireccion = false;
  mostrandoOpcionesRuta = false;
  mensajeRuta = '';
  origenRuta: GeolocationCoordinates | null = null;
  nuevoPunto: NuevoPuntoForm = this.formularioPuntoVacio();
  mapa: any | null = null;
  markers: any[] = [];
  isLeaflet = false;
  private markerRefs = new Map<PuntoMapa, { marker: any; infoWindow?: any }>();
  private activeInfoWindow: any | null = null;

  private readonly googleApiKey = 'YOUR_API_KEY';

  get refugiosListado(): PuntoMapa[] {
    if (!this.seleccionado) {
      return this.refugios;
    }

    return this.refugios.filter(refugio => refugio !== this.seleccionado);
  }

  ngOnInit(): void {
    this.cargarUbicaciones();
    this.transloco.langChanges$.subscribe(() => {
      this.updateMarkers();
      this.cdr.markForCheck();
    });
  }

  private cargarUbicaciones(mantenerSeleccion = false, enfocar?: { id?: number; latitud: number; longitud: number }): void {
    const seleccionadoId = mantenerSeleccion ? this.seleccionado?.id : undefined;
    this.cargandoUbicaciones = true;

    forkJoin({
      puntos: this.cargarPuntosSeguros(),
      refugios: this.safelink.listarRefugiosDisponibles().pipe(catchError(() => of([] as Refugio[]))),
    }).pipe(
      finalize(() => {
        this.cargandoUbicaciones = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: ({ puntos, refugios }) => {
        this.refugios = [
          ...(puntos ?? []).map(ubicacion => this.normalizarUbicacion(ubicacion, 'punto_seguro')),
          ...(refugios ?? []).map(ubicacion => this.normalizarUbicacion(ubicacion, 'refugio')),
        ]
          .filter(ubicacion => Number.isFinite(ubicacion.latitud) && Number.isFinite(ubicacion.longitud));
        const puntoEnfocado = enfocar
          ? this.refugios.find(ubicacion =>
              (enfocar.id !== undefined && (ubicacion.id === enfocar.id || ubicacion.idPunto === enfocar.id))
              || (Math.abs(ubicacion.latitud - enfocar.latitud) < 0.000001 && Math.abs(ubicacion.longitud - enfocar.longitud) < 0.000001)
            )
          : null;
        this.seleccionado = puntoEnfocado ?? this.refugios.find(ubicacion => ubicacion.id === seleccionadoId) ?? this.refugios[0] ?? null;
        this.updateMarkers();
        if (puntoEnfocado) {
          this.enfocarUbicacion(puntoEnfocado, 16, true);
        } else {
          this.centerMap();
        }
        if (!this.refugios.length) {
          this.snack.open(this.transloco.translate('mapa.noSeEncontraronPuntos'), this.transloco.translate('mapa.ok'), { duration: 3000 });
        }
        this.cdr.markForCheck();
      },
    });
  }

  private cargarPuntosSeguros(): Observable<Refugio[]> {
    const idUsuario = this.auth.getToken() ? this.auth.getIdUsuario() : null;

    if (!idUsuario) {
      return this.safelink.listarPuntosSeguros().pipe(catchError(() => of([] as Refugio[])));
    }

    return this.safelink.listarPuntosSegurosUsuarioMapa(idUsuario).pipe(
      catchError(() => {
        this.snack.open(this.transloco.translate('mapa.errorCargarPuntosPropios'), this.transloco.translate('mapa.ok'), { duration: 3500 });
        return this.safelink.listarPuntosSeguros().pipe(catchError(() => of([] as Refugio[])));
      })
    );
  }

  ngAfterViewInit(): void {
    if (this.googleApiKey === 'YOUR_API_KEY') {
      this.initLeafletMap();
      return;
    }

    this.loadGoogleMaps()
      .then(() => this.initMap())
      .catch(() => {
        this.snack.open(this.transloco.translate('mapa.errorCargarGoogleMaps'), this.transloco.translate('mapa.ok'), { duration: 4000 });
        this.initLeafletMap();
      });
  }

  private loadGoogleMaps(): Promise<void> {
    if (typeof google !== 'undefined' && google.maps) {
      return Promise.resolve();
    }

    return new Promise((resolve, reject) => {
      const existing = document.querySelector('script[data-google-maps]') as HTMLScriptElement;
      if (existing) {
        existing.addEventListener('load', () => resolve());
        existing.addEventListener('error', () => reject());
        return;
      }

      const script = document.createElement('script');
      script.src = `https://maps.googleapis.com/maps/api/js?key=${this.googleApiKey}`;
      script.async = true;
      script.defer = true;
      script.setAttribute('data-google-maps', 'true');
      script.onload = () => resolve();
      script.onerror = () => reject();
      document.head.appendChild(script);
    });
  }

  private initMap(): void {
    if (!this.mapContainer || typeof google === 'undefined' || !google.maps) {
      this.initLeafletMap();
      return;
    }

    this.isLeaflet = false;
    this.mapa = new google.maps.Map(this.mapContainer.nativeElement, {
      center: { lat: -12.09, lng: -77.02 },
      zoom: 12,
      mapTypeId: 'roadmap',
      streetViewControl: false,
      fullscreenControl: false,
      mapTypeControl: false,
    });

    this.mapa.addListener('click', (event: any) => this.zone.run(() => {
      if (event?.latLng) {
        this.seleccionarPuntoEnMapa(event.latLng.lat(), event.latLng.lng());
      }
    }));

    this.updateMarkers();
    this.centerMap();
  }

  private initLeafletMap(): void {
    if (!this.mapContainer || typeof L === 'undefined') {
      return;
    }

    this.isLeaflet = true;
    if (this.mapa && this.mapa.remove) {
      this.mapa.remove();
    }

    this.mapa = L.map(this.mapContainer.nativeElement).setView([-12.09, -77.02], 12);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
      maxZoom: 19,
    }).addTo(this.mapa);

    this.mapa.on('click', (event: any) => this.zone.run(() => {
      if (event?.latlng) {
        this.seleccionarPuntoEnMapa(event.latlng.lat, event.latlng.lng);
      }
    }));

    this.markers = [];
    this.updateMarkers();
    this.centerMap();
  }

  private updateMarkers(): void {
    if (!this.mapa) {
      return;
    }

    if (this.isLeaflet) {
      this.markers.forEach((marker: any) => marker.remove());
      this.markers = [];
      this.markerRefs.clear();

      this.refugios.forEach(refugio => {
        const marker = L.circleMarker([refugio.latitud, refugio.longitud], {
          radius: 8,
          fillColor: this.colorTipo(refugio.tipo, refugio),
          color: '#fff',
          weight: 2,
          fillOpacity: 0.9,
        }).addTo(this.mapa).bindPopup(this.popupContent(refugio));

        marker.on('click', (event: any) => this.zone.run(() => {
          if (typeof L !== 'undefined' && L.DomEvent) {
            L.DomEvent.stopPropagation(event);
          }
          if (this.modoAgregarPunto) {
            return;
          }
          this.seleccionar(refugio);
          this.enfocarUbicacion(refugio, 14, true);
        }));

        this.markers.push(marker);
        this.markerRefs.set(refugio, { marker });
      });
    } else {
      this.markers.forEach((marker: any) => marker.setMap(null));
      this.markers = [];
      this.markerRefs.clear();
      if (this.activeInfoWindow?.close) {
        this.activeInfoWindow.close();
      }
      this.activeInfoWindow = null;

      this.refugios.forEach(refugio => {
        const infoWindow = new google.maps.InfoWindow({
          content: this.popupContent(refugio),
        });
        const marker = new google.maps.Marker({
          position: { lat: refugio.latitud, lng: refugio.longitud },
          map: this.mapa,
          title: refugio.nombre,
          icon: {
            path: google.maps.SymbolPath.CIRCLE,
            fillColor: this.colorTipo(refugio.tipo, refugio),
            fillOpacity: 0.9,
            strokeWeight: 0,
            scale: 10,
          },
        });

        marker.addListener('click', () => this.zone.run(() => {
          if (this.modoAgregarPunto) {
            return;
          }
          this.seleccionar(refugio);
          this.enfocarUbicacion(refugio, 14, true);
        }));

        this.markers.push(marker);
        this.markerRefs.set(refugio, { marker, infoWindow });
      });
    }
  }

  private centerMap(): void {
    if (!this.mapa || !this.seleccionado) {
      return;
    }

    if (this.isLeaflet) {
      this.enfocarUbicacion(this.seleccionado, 13, false);
    } else {
      this.enfocarUbicacion(this.seleccionado, 13, false);
    }
  }

  seleccionar(r: PuntoMapa): void {
    this.seleccionado = r;
    this.resetRuta();
    this.centerMap();
    this.cdr.markForCheck();
  }

  seleccionarDesdeLista(r: PuntoMapa): void {
    this.seleccionado = r;
    this.resetRuta();
    this.enfocarUbicacion(r, 15, true);
    this.cdr.markForCheck();
  }

  verRuta(event?: Event): void {
    event?.stopPropagation();
    if (!this.seleccionado) {
      return;
    }

    this.mostrandoOpcionesRuta = false;
    this.mensajeRuta = '';
    this.origenRuta = null;

    if (!navigator.geolocation) {
      this.mensajeRuta = this.transloco.translate('mapa.geolocalizacionNoSoportada');
      this.snack.open(this.mensajeRuta, this.transloco.translate('mapa.ok'), { duration: 4000 });
      this.abrirRutaDestino();
      this.cdr.markForCheck();
      return;
    }

    navigator.geolocation.getCurrentPosition(
      position => this.zone.run(() => {
        this.origenRuta = position.coords;
        this.mostrandoOpcionesRuta = true;
        this.mensajeRuta = this.transloco.translate('mapa.elegirComoLlegar');
        this.cdr.markForCheck();
      }),
      () => this.zone.run(() => {
        this.mensajeRuta = this.transloco.translate('mapa.errorGeolocalizacion');
        this.mostrandoOpcionesRuta = false;
        this.snack.open(this.mensajeRuta, this.transloco.translate('mapa.ok'), { duration: 4000 });
        this.abrirRutaDestino();
        this.cdr.markForCheck();
      }),
      { enableHighAccuracy: true, timeout: 10000, maximumAge: 60000 }
    );
  }

  abrirRuta(modo: ModoRuta, event?: Event): void {
    event?.stopPropagation();
    if (!this.seleccionado) {
      return;
    }

    const destino = `${this.seleccionado.latitud},${this.seleccionado.longitud}`;
    const origen = this.origenRuta ? `${this.origenRuta.latitude},${this.origenRuta.longitude}` : '';
    const params = new URLSearchParams({
      api: '1',
      destination: destino,
      travelmode: modo,
    });

    if (origen) {
      params.set('origin', origen);
    }

    window.open(`https://www.google.com/maps/dir/?${params.toString()}`, '_blank', 'noopener,noreferrer');
  }

  activarModoAgregarPunto(event?: Event): void {
    event?.stopPropagation();
    const idUsuario = this.auth.getIdUsuario();

    if (!idUsuario) {
      this.snack.open(this.transloco.translate('mapa.iniciaSesionAgregarPunto'), this.transloco.translate('mapa.ok'), { duration: 3500 });
      return;
    }

    this.resetRuta();
    this.modoAgregarPunto = !this.modoAgregarPunto;
    this.confirmandoPunto = false;
    this.mostrandoFormularioPunto = false;
    this.puntoPendiente = null;
    this.nuevoPunto = this.formularioPuntoVacio();
    this.cdr.markForCheck();
  }

  cancelarAgregarPunto(event?: Event): void {
    event?.stopPropagation();
    this.limpiarFlujoAgregarPunto();
  }

  cancelarConfirmacionPunto(event?: Event): void {
    event?.stopPropagation();
    this.confirmandoPunto = false;
    this.puntoPendiente = null;
    this.cdr.markForCheck();
  }

  confirmarUbicacionPunto(event?: Event): void {
    event?.stopPropagation();
    if (!this.puntoPendiente) {
      return;
    }

    this.confirmandoPunto = false;
    this.resolviendoDireccion = true;
    this.obtenerDireccionAproximada(this.puntoPendiente.latitud, this.puntoPendiente.longitud)
      .then(direccion => this.zone.run(() => {
        if (this.puntoPendiente) {
          this.puntoPendiente = {
            ...this.puntoPendiente,
            direccion,
          };
        }
        this.resolviendoDireccion = false;
        this.mostrandoFormularioPunto = true;
        this.cdr.markForCheck();
      }));
  }

  guardarPuntoPropio(event?: Event): void {
    event?.stopPropagation();
    const idUsuario = this.auth.getIdUsuario();

    if (!idUsuario) {
      this.snack.open(this.transloco.translate('mapa.iniciaSesionAgregarPunto'), this.transloco.translate('mapa.ok'), { duration: 3500 });
      return;
    }

    if (!this.puntoPendiente) {
      this.snack.open(this.transloco.translate('mapa.selectUbicacionEnMapa'), this.transloco.translate('mapa.ok'), { duration: 3500 });
      return;
    }

    if (!this.nuevoPunto.nombre.trim() || !this.nuevoPunto.referenciaSeguridad.trim()) {
      this.snack.open(this.transloco.translate('mapa.completaNombreYReferencia'), this.transloco.translate('mapa.ok'), { duration: 3500 });
      return;
    }

    // Nota: este separador es un formato de datos interno usado por separarReferenciaSeguridad()
    // para parsear direcciones guardadas; no debe traducirse.
    const direccionConReferencia = `${this.puntoPendiente.direccion}. Referencia de seguridad: ${this.nuevoPunto.referenciaSeguridad.trim()}`;
    const punto: CrearPuntoSeguroRequest = {
      nombre: this.nuevoPunto.nombre.trim(),
      tipo: 'Punto seguro',
      direccion: direccionConReferencia,
      referencia: this.puntoPendiente.direccion,
      referenciaSeguridad: this.nuevoPunto.referenciaSeguridad.trim(),
      porQueEsSeguro: this.nuevoPunto.referenciaSeguridad.trim(),
      latitud: this.puntoPendiente.latitud,
      longitud: this.puntoPendiente.longitud,
      capacidad: 0,
    };
    const enfocar = {
      latitud: this.puntoPendiente.latitud,
      longitud: this.puntoPendiente.longitud,
    };

    this.guardandoPunto = true;
    this.safelink.crearPuntoSeguroUsuario(idUsuario, punto).pipe(
      finalize(() => {
        this.guardandoPunto = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: creado => {
        this.snack.open(this.transloco.translate('mapa.puntoAgregado'), this.transloco.translate('mapa.ok'), { duration: 3000 });
        const creadoNormalizado = creado ? this.normalizarUbicacion(creado as UbicacionApi, 'punto_seguro') : null;
        this.limpiarFlujoAgregarPunto();
        this.cargarUbicaciones(false, {
          id: creadoNormalizado?.id ?? creadoNormalizado?.idPunto,
          latitud: creadoNormalizado?.latitud ?? enfocar.latitud,
          longitud: creadoNormalizado?.longitud ?? enfocar.longitud,
        });
      },
      error: () => {
        this.snack.open(this.transloco.translate('mapa.errorGuardarPunto'), this.transloco.translate('mapa.ok'), { duration: 3500 });
      },
    });
  }

  eliminarPuntoPropio(refugio: PuntoMapa, event?: Event): void {
    event?.stopPropagation();
    const idUsuario = this.auth.getIdUsuario();
    const idPunto = refugio.idPunto ?? refugio.id;

    if (!refugio.propio || !idUsuario || !idPunto) {
      return;
    }

    if (!window.confirm(this.transloco.translate('mapa.confirmarEliminarPunto'))) {
      return;
    }

    this.eliminandoPunto = true;
    this.safelink.eliminarPuntoSeguroUsuario(idUsuario, idPunto).pipe(
      finalize(() => {
        this.eliminandoPunto = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: () => {
        this.snack.open(this.transloco.translate('mapa.puntoEliminado'), this.transloco.translate('mapa.ok'), { duration: 2500 });
        this.seleccionado = null;
        this.cargarUbicaciones();
      },
      error: () => {
        this.snack.open(this.transloco.translate('mapa.errorEliminarPunto'), this.transloco.translate('mapa.ok'), { duration: 3500 });
      },
    });
  }

  private seleccionarPuntoEnMapa(latitud: number, longitud: number): void {
    if (!this.modoAgregarPunto) {
      return;
    }

    this.puntoPendiente = {
      latitud,
      longitud,
      direccion: this.direccionFallback(latitud, longitud),
    };
    this.confirmandoPunto = true;
    this.mostrandoFormularioPunto = false;
    this.cdr.markForCheck();
  }

  private limpiarFlujoAgregarPunto(): void {
    this.modoAgregarPunto = false;
    this.confirmandoPunto = false;
    this.mostrandoFormularioPunto = false;
    this.resolviendoDireccion = false;
    this.puntoPendiente = null;
    this.nuevoPunto = this.formularioPuntoVacio();
    this.cdr.markForCheck();
  }

  private async obtenerDireccionAproximada(latitud: number, longitud: number): Promise<string> {
    const fallback = this.direccionFallback(latitud, longitud);

    try {
      const params = new URLSearchParams({
        format: 'jsonv2',
        lat: String(latitud),
        lon: String(longitud),
      });
      const response = await fetch(`https://nominatim.openstreetmap.org/reverse?${params.toString()}`, {
        headers: {
          Accept: 'application/json',
        },
      });

      if (!response.ok) {
        return fallback;
      }

      const data = await response.json() as { display_name?: string };
      return data.display_name?.trim() || fallback;
    } catch {
      return fallback;
    }
  }

  private direccionFallback(latitud: number, longitud: number): string {
    return `${this.transloco.translate('mapa.ubicacionSeleccionada')}: ${latitud.toFixed(6)}, ${longitud.toFixed(6)}`;
  }

  private abrirRutaDestino(): void {
    if (!this.seleccionado) {
      return;
    }

    const params = new URLSearchParams({
      api: '1',
      destination: `${this.seleccionado.latitud},${this.seleccionado.longitud}`,
    });
    window.open(`https://www.google.com/maps/dir/?${params.toString()}`, '_blank', 'noopener,noreferrer');
  }

  private resetRuta(): void {
    this.mostrandoOpcionesRuta = false;
    this.mensajeRuta = '';
    this.origenRuta = null;
  }

  private enfocarUbicacion(refugio: PuntoMapa, zoom: number, abrirPopup: boolean): void {
    if (!this.mapa) {
      return;
    }

    const ref = this.markerRefs.get(refugio);

    if (this.isLeaflet) {
      this.mapa.setView([refugio.latitud, refugio.longitud], zoom);
      if (abrirPopup && ref?.marker?.openPopup) {
        ref.marker.openPopup();
      }
      return;
    }

    this.mapa.panTo({ lat: refugio.latitud, lng: refugio.longitud });
    this.mapa.setZoom(zoom);

    if (abrirPopup && ref?.marker && ref?.infoWindow) {
      if (this.activeInfoWindow?.close) {
        this.activeInfoWindow.close();
      }
      ref.infoWindow.open(this.mapa, ref.marker);
      this.activeInfoWindow = ref.infoWindow;
    }
  }

  tipoBadge(tipo: string): string {
    const m: Record<string, string> = {
      punto_seguro: this.transloco.translate('mapa.tipoPuntoSeguro'),
      punto_encuentro: this.transloco.translate('mapa.tipoPuntoSeguro'),
      refugio: this.transloco.translate('mapa.tipoRefugio'),
      refugio_oficial: this.transloco.translate('mapa.tipoRefugio'),
      refugio_ampliado: this.transloco.translate('mapa.tipoRefugio'),
    };
    return m[tipo] ?? tipo;
  }

  iconoTipo(tipo: string): string {
    return tipo.includes('refugio') ? 'school' : 'park';
  }

  colorTipo(tipo: string, refugio?: PuntoMapa): string {
    if (refugio?.propio) {
      return '#e65100';
    }

    return tipo.includes('refugio') ? '#1565c0' : '#4caf50';
  }

  direccionTexto(refugio: Refugio): string {
    return refugio.direccion || refugio.referencia || this.transloco.translate('mapa.referenciaNoDefinida');
  }

  descripcionTexto(refugio: Refugio): string {
    return refugio.descripcion || '';
  }

  seguridadTexto(refugio: Refugio): string {
    if (refugio.porQueEsSeguro) {
      return refugio.porQueEsSeguro;
    }

    if (!refugio.descripcion) {
      return refugio.direccion || refugio.referencia || this.transloco.translate('mapa.referenciaSeguridadNoDefinida');
    }

    return '';
  }

  origenTexto(refugio: PuntoMapa): string {
    if (refugio.propio === true) {
      return this.transloco.translate('mapa.miPuntoSeguro');
    }

    if (refugio.sugerido === true) {
      return this.transloco.translate('mapa.sugeridoPorSafelink');
    }

    return '';
  }

  private normalizarUbicacion(ubicacion: UbicacionApi, tipoBase: string): PuntoMapa {
    const direccionRaw = ubicacion.direccion ?? ubicacion.ubicacion ?? ubicacion.referencia ?? ubicacion.puntoReferencia ?? '';
    const direccionPartes = this.separarReferenciaSeguridad(direccionRaw);
    const direccion = direccionPartes.direccion;
    const descripcion = ubicacion.descripcion ?? '';
    const esPuntoSeguro = this.normalizarTipo(ubicacion.tipo, tipoBase) === 'punto_seguro';
    const propio = ubicacion.propio === true;
    const sugerido = ubicacion.sugerido === true || (esPuntoSeguro && !propio);
    const porQueEsSeguro = ubicacion.porQueEsSeguro
      ?? ubicacion.porqueSeguro
      ?? ubicacion.porQueSeguro
      ?? ubicacion.referenciaSeguridad
      ?? ubicacion.motivoSeguridad
      ?? ubicacion.motivoSeguro
      ?? ubicacion.razonSeguridad
      ?? direccionPartes.referencia
      ?? '';

    return {
      id: this.numero(ubicacion.id ?? ubicacion.idPunto) ?? undefined,
      idPunto: this.numero(ubicacion.idPunto ?? ubicacion.id) ?? undefined,
      nombre: ubicacion.nombre ?? '',
      direccion,
      referencia: ubicacion.referencia ?? ubicacion.puntoReferencia ?? direccion,
      descripcion,
      porQueEsSeguro,
      tipo: this.normalizarTipo(ubicacion.tipo, tipoBase),
      latitud: Number(ubicacion.latitud ?? ubicacion.lat ?? ubicacion.latitude),
      longitud: Number(ubicacion.longitud ?? ubicacion.lng ?? ubicacion.longitude),
      capacidad: ubicacion.capacidad,
      activo: ubicacion.activo,
      disponible: ubicacion.disponible,
      sugerido,
      propio,
    };
  }

  private normalizarTipo(tipo: string | undefined, tipoBase: string): string {
    const valor = (tipo ?? tipoBase).toLowerCase();

    if (valor.includes('refugio')) {
      return 'refugio';
    }

    return 'punto_seguro';
  }

  private separarReferenciaSeguridad(direccion: string): { direccion: string; referencia?: string } {
    const separador = '. Referencia de seguridad: ';
    const indice = direccion.indexOf(separador);

    if (indice === -1) {
      return { direccion };
    }

    return {
      direccion: direccion.slice(0, indice).trim(),
      referencia: direccion.slice(indice + separador.length).trim(),
    };
  }

  private numero(value: number | string | undefined | null): number | null {
    if (value === undefined || value === null || value === '') {
      return null;
    }

    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : null;
  }

  private popupContent(refugio: Refugio): string {
    const descripcion = this.descripcionTexto(refugio);
    const seguridad = this.seguridadTexto(refugio);
    const origen = this.origenTexto(refugio as PuntoMapa);

    return `
      <strong>${this.escapeHtml(refugio.nombre)}</strong><br>
      <b>${this.escapeHtml(this.transloco.translate('mapa.tipoEtiqueta'))}</b> ${this.escapeHtml(this.tipoBadge(refugio.tipo))}<br>
      ${origen ? `<b>${this.escapeHtml(origen)}</b><br>` : ''}
      <b>${this.escapeHtml(this.transloco.translate('mapa.direccionOReferencia'))}</b> ${this.escapeHtml(this.direccionTexto(refugio))}
      ${descripcion ? `<br><b>${this.escapeHtml(this.transloco.translate('mapa.descripcion'))}</b> ${this.escapeHtml(descripcion)}` : ''}
      ${seguridad ? `<br><b>${this.escapeHtml(this.transloco.translate('mapa.referenciaSeguridad'))}</b> ${this.escapeHtml(seguridad)}` : ''}
    `;
  }

  private formularioPuntoVacio(): NuevoPuntoForm {
    return {
      nombre: '',
      referenciaSeguridad: '',
    };
  }

  private escapeHtml(value: string): string {
    return value
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');
  }
}
