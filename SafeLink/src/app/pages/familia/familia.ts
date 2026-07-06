import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs';
import { TranslocoModule, TranslocoService, provideTranslocoScope } from '@jsverse/transloco';
import { Auth } from '../../services/auth';
import { Familia as FamiliaService } from '../../services/familia';
import { Familiar } from '../../models/Familiar';

@Component({
  selector: 'app-familia',
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatIconModule, MatSelectModule, TranslocoModule],
  providers: [provideTranslocoScope('familia')],
  templateUrl: './familia.html',
  styleUrl: './familia.scss',
})
export class Familia implements OnInit {
  private fb = inject(FormBuilder);
  private snack = inject(MatSnackBar);
  private auth = inject(Auth);
  private familiaService = inject(FamiliaService);
  private cdr = inject(ChangeDetectorRef);
  private transloco = inject(TranslocoService);

  hidePhone = false;

  relaciones = ['Pareja', 'Hijo', 'Padre', 'Madre', 'Otro'];

  private relacionKeys: Record<string, string> = {
    Pareja: 'familia.relaciones.pareja',
    Hijo: 'familia.relaciones.hijo',
    Padre: 'familia.relaciones.padre',
    Madre: 'familia.relaciones.madre',
    Otro: 'familia.relaciones.otro',
  };

  relacionKey(relacion: string): string {
    return this.relacionKeys[relacion] ?? 'familia.relaciones.otro';
  }

  form = this.fb.group({
    nombre: ['', [Validators.required, Validators.maxLength(50)]],
    telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{7,15}$/)]],
    relacion: ['Pareja', [Validators.required]],
    ubicacionHabitual: [''],
  });

  familiares: Familiar[] = [];
  cargandoFamilia = false;
  guardandoFamiliar = false;

  ngOnInit(): void {
    this.cargarFamilia();
  }

  cargarFamilia(): void {
    const idUsuario = this.auth.getIdUsuario();
    if (!idUsuario) {
      this.familiares = [];
      this.cargandoFamilia = false;
      this.cdr.markForCheck();
      return;
    }

    this.cargandoFamilia = true;
    this.cdr.markForCheck();
    this.familiaService.listarPorUsuario(idUsuario).pipe(
      finalize(() => {
        this.cargandoFamilia = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: familiares => {
        this.familiares = familiares ?? [];
        this.cdr.markForCheck();
      },
      error: () => {
        this.familiares = [];
        this.snack.open(this.transloco.translate('familia.mensajes.errorCargaFamilia'), 'OK', { duration: 3000 });
        this.cdr.markForCheck();
      },
    });
  }

  agregarFamiliar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const idUsuario = this.auth.getIdUsuario();
    if (!idUsuario) {
      this.snack.open(this.transloco.translate('familia.mensajes.sesionNoEncontrada'), 'OK', { duration: 3500 });
      return;
    }

    const value = this.form.getRawValue();
    const body = {
      nombre: value.nombre ?? '',
      telefono: value.telefono ?? '',
      relacion: value.relacion ?? '',
      ubicacionHabitual: value.ubicacionHabitual ?? '',
    };

    this.guardandoFamiliar = true;
    this.cdr.markForCheck();
    this.familiaService.registrarFamiliar(idUsuario, body).pipe(
      finalize(() => {
        this.guardandoFamiliar = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: () => {
        this.snack.open(this.transloco.translate('familia.mensajes.familiarRegistrado'), 'OK', { duration: 2500 });
        this.form.reset({
          nombre: '',
          telefono: '',
          relacion: 'Pareja',
          ubicacionHabitual: '',
        });
        this.cargarFamilia();
        this.cdr.markForCheck();
      },
      error: () => {
        this.snack.open(this.transloco.translate('familia.mensajes.errorRegistro'), 'OK', { duration: 4000 });
        this.cdr.markForCheck();
      },
    });
  }
}
