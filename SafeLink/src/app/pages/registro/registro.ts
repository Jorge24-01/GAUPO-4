import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import {
  FormBuilder, ReactiveFormsModule, Validators,
  AbstractControl, ValidationErrors,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslocoModule, TranslocoService, provideTranslocoScope } from '@jsverse/transloco';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Auth } from '../../services/auth';

function passwordsIguales(ctrl: AbstractControl): ValidationErrors | null {
  const pass = ctrl.get('password')?.value;
  const pass2 = ctrl.get('password2')?.value;
  return pass && pass2 && pass !== pass2 ? { noCoinciden: true } : null;
}

@Component({
  selector: 'app-registro',
  imports: [CommonModule, ReactiveFormsModule, RouterLink,
    MatFormFieldModule, MatInputModule, MatSelectModule, MatCheckboxModule,
    MatButtonModule, MatIconModule, TranslocoModule],
  providers: [provideTranslocoScope('registro')],
  templateUrl: './registro.html',
  styleUrl: './registro.scss',
})
export class Registro {
  private fb = inject(FormBuilder);
  private auth = inject(Auth);
  private snack = inject(MatSnackBar);
  private router = inject(Router);
  private transloco = inject(TranslocoService);
  private liveAnnouncer = inject(LiveAnnouncer);

  hidePass = true; hidePass2 = true;
  errorRegistro = '';
  exitoRegistro = '';
  tiposUsuario = [
    { valor: 'RESIDENTE', etiqueta: 'registro.tipoResidente' },
    { valor: 'AUTORIDAD', etiqueta: 'registro.tipoAutoridad' },
    { valor: 'VOLUNTARIO', etiqueta: 'registro.tipoVoluntario' },
    { valor: 'COORDINADOR', etiqueta: 'registro.tipoCoordinador' },
  ];

  form = this.fb.group({
    nombre: ['', [Validators.required, Validators.maxLength(50)]],
    apellido: ['', [Validators.required, Validators.maxLength(50)]],
    email: ['', [Validators.required, Validators.email]],
    telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
    distrito: ['', [Validators.required]],
    tipoUsuario: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    password2: ['', [Validators.required]],
    terminos: [false, [Validators.requiredTrue]],
  }, { validators: passwordsIguales });

  registrar(): void {
    this.errorRegistro = '';
    this.exitoRegistro = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.errorRegistro = this.form.controls.terminos.invalid
        ? this.transloco.translate('registro.terminosRequerido')
        : this.transloco.translate('registro.revisaCampos');
      this.liveAnnouncer.announce(this.errorRegistro);
      return;
    }

    const v = this.form.value;
    this.auth.registro({
      nombre: v.nombre!,
      apellido: v.apellido!,
      correo: v.email!,
      telefono: v.telefono!,
      distrito: v.distrito!,
      contrasena: v.password!,
      tipoUsuario: v.tipoUsuario!,
    }).subscribe({
      next: () => {
        this.exitoRegistro = this.transloco.translate('registro.cuentaCreadaExito');
        const cuentaCreadaCorta = this.transloco.translate('registro.cuentaCreadaCorta');
        const ok = this.transloco.translate('registro.ok');
        this.snack.open(cuentaCreadaCorta, ok, { duration: 2500 });
        this.liveAnnouncer.announce(this.exitoRegistro);
        setTimeout(() => this.router.navigate(['/login']), 1200);
      },
      error: (err: HttpErrorResponse) => {
        this.errorRegistro = this.obtenerMensajeError(err);
        const ok = this.transloco.translate('registro.ok');
        this.snack.open(this.errorRegistro, ok, { duration: 3500 });
        this.liveAnnouncer.announce(this.errorRegistro);
      },
    });
  }

  private obtenerMensajeError(err: HttpErrorResponse): string {
    if (typeof err.error === 'string' && err.error.trim()) {
      return err.error;
    }

    if (err.error?.message) {
      return err.error.message;
    }

    if (err.status === 0) {
      return this.transloco.translate('registro.noConectaBackend');
    }

    return this.transloco.translate('registro.errorGenerico');
  }
}
