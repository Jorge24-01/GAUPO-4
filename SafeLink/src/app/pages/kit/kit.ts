import { ChangeDetectorRef, Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule, TranslocoService, provideTranslocoScope } from '@jsverse/transloco';
import { catchError, finalize, of, Subscription, switchMap } from 'rxjs';
import { Auth } from '../../services/auth';
import { ItemKit, Safelink } from '../../services/safelink';

interface KitItemView {
  icon: string;
  title: string;
  description: string;
  category: string;
  quantity: string;
  status: string;
}

@Component({
  selector: 'app-kit',
  imports: [CommonModule, MatIconModule, TranslocoModule],
  providers: [provideTranslocoScope('kit')],
  templateUrl: './kit.html',
  styleUrl: './kit.scss',
})
export class Kit implements OnInit, OnDestroy {
  private auth = inject(Auth);
  private safelink = inject(Safelink);
  private cdr = inject(ChangeDetectorRef);
  private transloco = inject(TranslocoService);
  private langSub?: Subscription;

  items: KitItemView[] = [];
  itemsCrudos: (ItemKit | string)[] = [];
  estadoLista: 'ok' | 'vacio' | 'error' = 'ok';
  cargando = true;
  mensaje = '';

  ngOnInit(): void {
    // El backend resuelve nombreItem segun el header Accept-Language, asi que hay que
    // volver a pedir los items cada vez que cambia el idioma (igual que en inicio.ts).
    this.langSub = this.transloco.langChanges$.subscribe(() => {
      this.cargarItems();
    });
  }

  ngOnDestroy(): void {
    this.langSub?.unsubscribe();
  }

  private cargarItems(): void {
    this.cargando = true;
    const idUsuario = this.auth.getToken() ? this.auth.getIdUsuario() : null;

    const items$ = idUsuario
      ? this.safelink.listarKitsPorUsuario(idUsuario).pipe(
        switchMap(kits => {
          const idKit = kits[0]?.id ?? kits[0]?.idKit;
          if (!idKit) {
            return this.cargarRecomendados();
          }

          return this.safelink.listarItemsKit(idKit).pipe(
            switchMap(items => items.length ? of(items) : this.cargarRecomendados()),
            catchError(() => this.cargarRecomendados())
          );
        }),
        catchError(() => this.cargarRecomendados())
      )
      : this.cargarRecomendados();

    items$.pipe(
      finalize(() => {
        this.cargando = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: items => {
        this.itemsCrudos = items;
        this.items = items.map(item => this.toView(item));
        this.estadoLista = this.items.length ? 'ok' : 'vacio';
        this.mensaje = this.mensajeParaEstado(this.estadoLista);
        this.cdr.markForCheck();
      },
      error: () => {
        this.itemsCrudos = [];
        this.items = [];
        this.estadoLista = 'error';
        this.mensaje = this.mensajeParaEstado(this.estadoLista);
        this.cdr.markForCheck();
      },
    });
  }

  private mensajeParaEstado(estado: 'ok' | 'vacio' | 'error'): string {
    if (estado === 'vacio') {
      return this.transloco.translate('kit.sinRecomendaciones');
    }
    if (estado === 'error') {
      return this.transloco.translate('kit.errorCarga');
    }
    return '';
  }

  private cargarRecomendados() {
    return this.safelink.listarItemsKitRecomendados();
  }

  private toView(item: ItemKit | string): KitItemView {
    const itemNormalizado: ItemKit = typeof item === 'string' ? { nombre: item } : item;
    const nombre = itemNormalizado.titulo ?? itemNormalizado.nombreItem ?? itemNormalizado.nombre ?? '';
    const categoria = itemNormalizado.categoria ?? 'Recomendado';
    const categoriaMostrada = this.categoriaTraducida(categoria);
    const cantidadValor = itemNormalizado.cantidadRecomendada ?? itemNormalizado.cantidad;
    const estado = itemNormalizado.estado ?? this.estadoTraducido(this.estadoCompleto(itemNormalizado));

    return {
      icon: itemNormalizado.icono ?? itemNormalizado.icon ?? this.iconoCategoria(categoria, nombre),
      title: nombre,
      description: itemNormalizado.descripcion || this.descripcionGenerica(nombre, categoriaMostrada),
      category: categoriaMostrada,
      quantity: cantidadValor !== undefined && cantidadValor !== null
        ? this.transloco.translate('kit.cantidadRecomendada', { cantidad: cantidadValor })
        : '',
      status: estado ? this.transloco.translate('kit.estadoEtiqueta', { estado }) : '',
    };
  }

  private categoriaTraducida(categoria: string): string {
    const clave = categoria.trim().toLowerCase();
    const traduccion = this.transloco.translate(`kit.categorias.${clave}`);
    return traduccion !== `kit.categorias.${clave}` ? traduccion : categoria;
  }

  private estadoCompleto(item: ItemKit): 'completo' | 'pendiente' | 'disponible' | 'recomendado' | '' {
    if (item.completo === true || item.completado === true) {
      return 'completo';
    }

    if (item.completo === false || item.completado === false) {
      return 'pendiente';
    }

    if (item.tieneItem === true) {
      return 'disponible';
    }

    if (item.tieneItem === false) {
      return 'recomendado';
    }

    return '';
  }

  private estadoTraducido(clave: 'completo' | 'pendiente' | 'disponible' | 'recomendado' | ''): string {
    return clave ? this.transloco.translate(`kit.estado.${clave}`) : '';
  }

  private descripcionGenerica(nombre: string, categoria: string): string {
    const base = nombre || this.transloco.translate('kit.elementoGenerico');
    return this.transloco.translate('kit.descripcionGenerica', { base, categoria: categoria.toLowerCase() });
  }

  private iconoCategoria(categoria?: string, nombre?: string): string {
    // El nombre puede venir resuelto en espanol o en ingles segun el idioma activo
    // (el backend traduce nombreItem), asi que cada palabra clave se revisa en ambos idiomas.
    const valor = `${categoria ?? ''} ${nombre ?? ''}`.toLowerCase();
    if (valor.includes('agua') || valor.includes('water')) return 'water_drop';
    if (valor.includes('salud') || valor.includes('medic')) return 'medical_services';
    if (valor.includes('alimento') || valor.includes('comida') || valor.includes('food')) return 'restaurant';
    if (valor.includes('linterna') || valor.includes('luz') || valor.includes('energia') || valor.includes('flashlight')) return 'flashlight_on';
    if (valor.includes('radio')) return 'settings_input_antenna';
    if (valor.includes('document')) return 'article';
    if (valor.includes('manta') || valor.includes('blanket')) return 'bed';
    if (valor.includes('cargador') || valor.includes('bateria') || valor.includes('charger') || valor.includes('battery')) return 'battery_charging_full';
    if (valor.includes('mascar') || valor.includes('mask')) return 'masks';
    if (valor.includes('silbato') || valor.includes('whistle')) return 'campaign';
    return 'inventory_2';
  }
}
