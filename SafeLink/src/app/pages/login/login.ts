import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule, TranslocoService, provideTranslocoScope } from '@jsverse/transloco';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [ CommonModule, ReactiveFormsModule, RouterLink,
    MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, TranslocoModule],
  providers: [provideTranslocoScope('login')],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
   private fb     = inject(FormBuilder);
  private auth   = inject(Auth);
  private snack  = inject(MatSnackBar);
  private router = inject(Router);
  private transloco = inject(TranslocoService);
  private liveAnnouncer = inject(LiveAnnouncer);

  hidePass = true;

  form = this.fb.group({
    email:    ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });

  ingresar(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const { email, password } = this.form.value;
    this.auth.login({ correo: email!, contrasena: password! }).subscribe({
      next: () => {
        this.transloco.selectTranslate('bienvenidaExitosa', {}, 'login').subscribe(texto => {
          const ok = this.transloco.translate('ok', {}, 'login');
          this.snack.open(texto, ok, { duration: 2500 });
        });
        this.router.navigate(['/inicio']);
      },
      error: () => {
        this.transloco.selectTranslate('credencialesIncorrectas', {}, 'login').subscribe(texto => {
          const ok = this.transloco.translate('ok', {}, 'login');
          this.snack.open(texto, ok, { duration: 3000 });
          this.liveAnnouncer.announce(texto);
        });
      },
    });
  }
}
