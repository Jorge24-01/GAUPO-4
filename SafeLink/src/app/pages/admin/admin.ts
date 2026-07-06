import { ChangeDetectorRef, Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { catchError, finalize, forkJoin, of } from 'rxjs';
import { TranslocoModule, TranslocoService, provideTranslocoScope } from '@jsverse/transloco';
import { Admin, FamiliaAdmin } from '../../services/admin';
import { Auth } from '../../services/auth';
import { Usuario } from '../../models/usuario';

@Component({
  selector: 'app-admin',
  imports: [CommonModule, MatCardModule, MatIconModule, MatButtonModule, TranslocoModule],
  providers: [provideTranslocoScope('admin')],
  templateUrl: './admin.html',
  styleUrl: './admin.scss',
})
export class AdminPage implements OnInit {
  private admin = inject(Admin);
  private cdr = inject(ChangeDetectorRef);
  private transloco = inject(TranslocoService);
  auth = inject(Auth);

  usuarios = signal<Usuario[]>([]);
  familias = signal<FamiliaAdmin[]>([]);
  cargando = signal(true);
  error = signal('');
  eliminandoId = signal<number | null>(null);

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando.set(true);
    this.error.set('');
    this.cdr.markForCheck();

    forkJoin({
      usuarios: this.admin.listarUsuarios().pipe(catchError((err: HttpErrorResponse) => {
        this.manejarErrorAdmin(err, this.transloco.translate('admin.errorCargarUsuarios'));
        return of([] as Usuario[]);
      })),
      familias: this.admin.listarFamilias().pipe(catchError((err: HttpErrorResponse) => {
        this.manejarErrorAdmin(err, this.transloco.translate('admin.errorCargarFamilias'));
        return of([] as FamiliaAdmin[]);
      })),
    }).subscribe({
      next: ({ usuarios, familias }) => {
        this.usuarios.set(usuarios);
        this.familias.set(familias);
        this.cdr.markForCheck();
      },
      complete: () => {
        this.cargando.set(false);
        this.cdr.markForCheck();
      },
    });
  }

  familiasDelUsuario(usuario: Usuario): FamiliaAdmin[] {
    const idUsuario = usuario.idUsuario ?? usuario.usuarioId ?? usuario.id;
    const correo = usuario.correo ?? usuario.email;

    return this.familias().filter(familia => {
      const familiaUsuarioId = familia.idUsuario ?? familia.usuarioId;
      const familiaCorreo = familia.usuarioCorreo;

      return (idUsuario !== undefined && familiaUsuarioId === idUsuario)
        || (!!correo && familiaCorreo === correo);
    });
  }

  puedeEliminarUsuario(usuario: Usuario): boolean {
    return this.auth.isAdmin()
      && this.idCuenta(usuario) !== null
      && (usuario.correo ?? usuario.email ?? '').toLowerCase() !== 'demo@safelink.com';
  }

  eliminarCuenta(usuario: Usuario): void {
    if (!this.puedeEliminarUsuario(usuario)) {
      return;
    }

    const id = this.idCuenta(usuario);
    if (id === null) {
      this.error.set(this.transloco.translate('admin.errorIdentificarCuenta'));
      this.cdr.markForCheck();
      return;
    }

    const confirmado = window.confirm(this.transloco.translate('admin.confirmarEliminarCuenta'));
    if (!confirmado) {
      return;
    }

    this.error.set('');
    this.eliminandoId.set(id);
    this.cdr.markForCheck();
    this.admin.eliminarUsuario(id).pipe(
      finalize(() => {
        this.eliminandoId.set(null);
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: () => this.cargarDatos(),
      error: (err: HttpErrorResponse) => this.manejarErrorAdmin(err, this.transloco.translate('admin.errorEliminarCuenta')),
    });
  }

  idCuenta(usuario: Usuario): number | null {
    const id = usuario.idUsuario ?? usuario.usuarioId ?? usuario.id;
    return id !== undefined && id !== null ? id : null;
  }

  private manejarErrorAdmin(err: HttpErrorResponse, fallback: string): void {
    if (err.status === 403) {
      this.error.set(this.transloco.translate('admin.accesoExclusivoAdmin'));
      this.cdr.markForCheck();
      return;
    }

    if (!this.error()) {
      this.error.set(fallback);
      this.cdr.markForCheck();
    }
  }
}
