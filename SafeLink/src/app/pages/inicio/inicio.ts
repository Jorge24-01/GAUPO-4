import { ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { finalize, Subscription } from 'rxjs';
import { TranslocoModule, TranslocoService, provideTranslocoScope } from '@jsverse/transloco';
import { ApiCard, Safelink } from '../../services/safelink';
import { Auth } from '../../services/auth';

interface ConsejoView {
  icon: string;
  title: string;
  desc: string;
  category: string;
  variant: number;
}

@Component({
  selector: 'app-inicio',
  imports: [CommonModule, RouterLink, MatButtonModule, MatIconModule, TranslocoModule],
  providers: [provideTranslocoScope('inicio')],
  templateUrl: './inicio.html',
  styleUrl: './inicio.scss',
})
export class Inicio implements OnInit, OnDestroy {
  private safelink = inject(Safelink);
  private cdr = inject(ChangeDetectorRef);
  private transloco = inject(TranslocoService);
  auth = inject(Auth);

  features: ConsejoView[] = [];
  private consejosCrudos: ApiCard[] = [];
  cargandoConsejos = true;
  mensajeConsejos = '';
  private estadoMensajeConsejos: 'ninguno' | 'sinConsejos' | 'errorCarga' = 'ninguno';
  indiceConsejoActual = 0;
  consejoDesbloqueado = false;
  progresoRaspado = 0;
  estaRaspando = false;

  private scratchCanvas?: ElementRef<HTMLCanvasElement>;
  private raspadoTimer: ReturnType<typeof setInterval> | null = null;
  private ultimoTickRaspado = 0;
  private ultimoMovimientoRaspado = 0;
  private langChangeSub?: Subscription;

  @ViewChild('scratchCanvas')
  set scratchCanvasRef(ref: ElementRef<HTMLCanvasElement> | undefined) {
    this.scratchCanvas = ref;
    if (ref) {
      requestAnimationFrame(() => this.prepararCanvasRaspado());
    }
  }

  ngOnInit(): void {
    // El backend resuelve `contenido` segun el header Accept-Language, asi que hay que
    // volver a pedir los consejos cada vez que cambia el idioma (no basta con remapear
    // los datos ya cargados, porque ese texto queda fijado al idioma con el que se pidio).
    this.langChangeSub = this.transloco.langChanges$.subscribe(() => {
      this.cargarConsejos();
      requestAnimationFrame(() => this.prepararCanvasRaspado());
    });
  }

  private cargarConsejos(): void {
    this.cargandoConsejos = true;
    this.safelink.listarConsejos().pipe(
      finalize(() => {
        this.cargandoConsejos = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: consejos => {
        this.consejosCrudos = [...(consejos ?? [])]
          .sort((a, b) => (a.ordenVisualizacion ?? 0) - (b.ordenVisualizacion ?? 0));
        this.features = this.consejosCrudos.map((consejo, index) => this.toFeature(consejo, index));
        this.estadoMensajeConsejos = this.features.length ? 'ninguno' : 'sinConsejos';
        this.actualizarMensajeConsejos();
        this.cdr.markForCheck();
      },
      error: () => {
        this.consejosCrudos = [];
        this.features = [];
        this.estadoMensajeConsejos = 'errorCarga';
        this.actualizarMensajeConsejos();
        this.cdr.markForCheck();
      },
    });
  }

  private actualizarMensajeConsejos(): void {
    this.mensajeConsejos =
      this.estadoMensajeConsejos === 'ninguno'
        ? ''
        : this.transloco.translate(`inicio.features.${this.estadoMensajeConsejos}`);
  }

  ngOnDestroy(): void {
    this.detenerRaspado();
    this.langChangeSub?.unsubscribe();
  }

  get consejoActual(): ConsejoView {
    return this.features[this.indiceConsejoActual] ?? {
      icon: 'health_and_safety',
      title: '',
      desc: '',
      category: this.transloco.translate('inicio.features.categoriaPreparacion'),
      variant: 0,
    };
  }

  anteriorConsejo(): void {
    if (!this.features.length) return;
    this.indiceConsejoActual = (this.indiceConsejoActual - 1 + this.features.length) % this.features.length;
    this.reiniciarRaspado();
  }

  siguienteConsejo(): void {
    if (!this.features.length) return;
    this.indiceConsejoActual = (this.indiceConsejoActual + 1) % this.features.length;
    this.reiniciarRaspado();
  }

  irAConsejo(index: number): void {
    if (index === this.indiceConsejoActual || index < 0 || index >= this.features.length) return;
    this.indiceConsejoActual = index;
    this.reiniciarRaspado();
  }

  iniciarRaspado(event?: PointerEvent): void {
    if (this.consejoDesbloqueado || this.estaRaspando) return;

    event?.preventDefault();
    if (event) {
      try {
        (event.currentTarget as HTMLElement | null)?.setPointerCapture?.(event.pointerId);
      } catch {
        // Pointer capture is only available while the pointer is actively pressed.
      }
      this.borrarZonaRaspado(event);
    }
    this.estaRaspando = true;
    this.ultimoTickRaspado = performance.now();
    this.raspadoTimer = setInterval(() => this.avanzarRaspado(), 60);
  }

  raspar(event: PointerEvent): void {
    if (this.consejoDesbloqueado || !this.estaRaspando) return;
    event.preventDefault();
    this.borrarZonaRaspado(event);
  }

  detenerRaspado(): void {
    this.estaRaspando = false;
    if (this.raspadoTimer) {
      clearInterval(this.raspadoTimer);
      this.raspadoTimer = null;
    }
  }

  private avanzarRaspado(): void {
    const ahora = performance.now();
    const delta = ahora - this.ultimoTickRaspado;
    this.ultimoTickRaspado = ahora;
    if (ahora - this.ultimoMovimientoRaspado > 220) return;

    this.progresoRaspado = Math.min(100, this.progresoRaspado + (delta / 2000) * 100);

    if (this.progresoRaspado >= 100) {
      this.progresoRaspado = 100;
      this.consejoDesbloqueado = true;
      this.detenerRaspado();
    }

    this.cdr.markForCheck();
  }

  private reiniciarRaspado(): void {
    this.detenerRaspado();
    this.consejoDesbloqueado = false;
    this.progresoRaspado = 0;
    this.cdr.markForCheck();
    requestAnimationFrame(() => this.prepararCanvasRaspado());
  }

  private prepararCanvasRaspado(): void {
    const canvas = this.scratchCanvas?.nativeElement;
    if (!canvas || this.consejoDesbloqueado) return;

    const rect = canvas.getBoundingClientRect();
    const escala = window.devicePixelRatio || 1;
    canvas.width = Math.max(1, Math.round(rect.width * escala));
    canvas.height = Math.max(1, Math.round(rect.height * escala));

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    ctx.setTransform(escala, 0, 0, escala, 0, 0);
    const fondo = ctx.createLinearGradient(0, 0, rect.width, rect.height);
    fondo.addColorStop(0, 'rgba(31, 116, 55, 0.97)');
    fondo.addColorStop(1, 'rgba(9, 68, 28, 0.97)');
    ctx.globalCompositeOperation = 'source-over';
    ctx.fillStyle = fondo;
    ctx.fillRect(0, 0, rect.width, rect.height);

    const brillo = ctx.createLinearGradient(0, 0, rect.width, 0);
    brillo.addColorStop(0, 'rgba(255, 255, 255, 0.12)');
    brillo.addColorStop(0.38, 'rgba(255, 255, 255, 0)');
    brillo.addColorStop(1, 'rgba(255, 255, 255, 0.08)');
    ctx.fillStyle = brillo;
    ctx.fillRect(0, 0, rect.width, rect.height);

    this.dibujarContenidoCubierta(ctx, rect.width, rect.height);
  }

  private borrarZonaRaspado(event: PointerEvent): void {
    const canvas = this.scratchCanvas?.nativeElement;
    const ctx = canvas?.getContext('2d');
    if (!canvas || !ctx) return;

    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    const radio = 42;

    ctx.globalCompositeOperation = 'destination-out';
    const borrado = ctx.createRadialGradient(x, y, radio * 0.25, x, y, radio);
    borrado.addColorStop(0, 'rgba(0, 0, 0, 1)');
    borrado.addColorStop(0.72, 'rgba(0, 0, 0, 0.86)');
    borrado.addColorStop(1, 'rgba(0, 0, 0, 0)');
    ctx.fillStyle = borrado;
    ctx.beginPath();
    ctx.arc(x, y, radio, 0, Math.PI * 2);
    ctx.fill();

    this.ultimoMovimientoRaspado = performance.now();
    this.cdr.markForCheck();
  }

  private dibujarContenidoCubierta(ctx: CanvasRenderingContext2D, width: number, height: number): void {
    const centroX = width / 2;
    const centroY = height / 2;

    ctx.save();
    ctx.globalCompositeOperation = 'source-over';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';

    ctx.fillStyle = 'rgba(255, 255, 255, 0.12)';
    ctx.beginPath();
    ctx.arc(centroX, centroY - 76, 46, 0, Math.PI * 2);
    ctx.fill();

    ctx.strokeStyle = 'rgba(255, 255, 255, 0.82)';
    ctx.lineWidth = 4;
    ctx.lineCap = 'round';
    ctx.lineJoin = 'round';
    ctx.beginPath();
    ctx.arc(centroX - 8, centroY - 84, 13, Math.PI * 0.08, Math.PI * 1.55);
    ctx.moveTo(centroX + 7, centroY - 84);
    ctx.lineTo(centroX + 22, centroY - 69);
    ctx.moveTo(centroX + 22, centroY - 69);
    ctx.lineTo(centroX + 11, centroY - 58);
    ctx.stroke();

    ctx.fillStyle = '#ffffff';
    ctx.font = '700 22px Roboto, Arial, sans-serif';
    this.dibujarTextoCentrado(
      ctx,
      this.transloco.translate('inicio.features.raspaParaDesbloquear'),
      centroX,
      centroY + 4,
      Math.min(380, width - 48),
      28
    );

    ctx.fillStyle = 'rgba(255, 255, 255, 0.86)';
    ctx.font = '500 13px Roboto, Arial, sans-serif';
    this.dibujarTextoCentrado(
      ctx,
      this.transloco.translate('inicio.features.mantenPresionado'),
      centroX,
      centroY + 44,
      Math.min(360, width - 52),
      20
    );

    ctx.restore();
  }

  private dibujarTextoCentrado(
    ctx: CanvasRenderingContext2D,
    texto: string,
    x: number,
    y: number,
    anchoMaximo: number,
    altoLinea: number
  ): void {
    const palabras = texto.split(' ');
    const lineas: string[] = [];
    let lineaActual = '';

    for (const palabra of palabras) {
      const prueba = lineaActual ? `${lineaActual} ${palabra}` : palabra;
      if (ctx.measureText(prueba).width > anchoMaximo && lineaActual) {
        lineas.push(lineaActual);
        lineaActual = palabra;
      } else {
        lineaActual = prueba;
      }
    }

    if (lineaActual) {
      lineas.push(lineaActual);
    }

    const inicioY = y - ((lineas.length - 1) * altoLinea) / 2;
    lineas.forEach((linea, index) => {
      ctx.fillText(linea, x, inicioY + index * altoLinea);
    });
  }

  private toFeature(card: ApiCard, index: number): ConsejoView {
    const fallbackIcons = ['health_and_safety', 'emergency_home', 'checklist'];

    return {
      icon: card.icono ?? card.icon ?? fallbackIcons[index % fallbackIcons.length],
      title:
        card.titulo ??
        card.title ??
        card.nombre ??
        this.tituloDesdeCategoria(card.categoria) ??
        `${this.transloco.translate('inicio.features.categoriaPreparacion')} ${index + 1}`,
      desc: card.contenido ?? card.descripcion ?? card.description ?? card.texto ?? '',
      category:
        this.tituloDesdeCategoria(card.categoria ?? card.tipoDesastre) ??
        this.transloco.translate('inicio.features.categoriaPreparacion'),
      variant: index % 3,
    };
  }

  private tituloDesdeCategoria(categoria: string | undefined): string | undefined {
    if (!categoria) {
      return undefined;
    }

    const clave = categoria.trim().toLowerCase();
    const traduccion = this.transloco.translate(`inicio.features.categorias.${clave}`);
    if (traduccion !== `inicio.features.categorias.${clave}`) {
      return traduccion;
    }

    return categoria
      .split('-')
      .filter(Boolean)
      .map(parte => parte.charAt(0).toUpperCase() + parte.slice(1))
      .join(' ');
  }
}
