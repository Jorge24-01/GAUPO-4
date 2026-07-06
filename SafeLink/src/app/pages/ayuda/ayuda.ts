import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { HttpClient } from '@angular/common/http';
import { TranslocoModule, TranslocoService, provideTranslocoScope } from '@jsverse/transloco';
import { Faq, Safelink } from '../../services/safelink';
import { environment } from '../../../environments/environment';

interface ChatMessage {
  sender: 'user' | 'bot';
  text: string;
}

interface AsistenteResponse {
  respuesta?: string;
}

@Component({
  selector: 'app-ayuda',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    TranslocoModule,
  ],
  providers: [provideTranslocoScope('ayuda')],
  templateUrl: './ayuda.html',
  styleUrl: './ayuda.scss',
})
export class Ayuda implements OnInit {
  private fb = inject(FormBuilder);
  private safelink = inject(Safelink);
  private http = inject(HttpClient);
  private transloco = inject(TranslocoService);

  faqs: Faq[] = [];
  cargando = false;

  messages = signal<ChatMessage[]>([]);

  form = this.fb.group({
    pregunta: ['', [Validators.required, Validators.minLength(2)]],
  });

  ngOnInit(): void {
    this.safelink.listarFaq().subscribe({
      next: (faqs) => {
        this.faqs = faqs;
      },
      error: () => {
        this.faqs = [];
      },
    });
  }

  enviar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const texto = this.form.value.pregunta?.trim();
    if (!texto) return;
    this.messages.set([...this.messages(), { sender: 'user', text: texto }]);
    this.form.reset();
    this.mandarRespuesta(texto);
  }

  clearChat(): void {
    this.messages.set([]);
  }

  private mandarRespuesta(texto: string): void {
    this.cargando = true;

    this.http
      .post<AsistenteResponse>(`${environment.apiUrl}/asistente/preguntar`, { pregunta: texto })
      .subscribe({
        next: (res) => {
          const respuesta =
            res?.respuesta ??
            this.transloco.translate('ayuda.noPudeGenerar') ??
            'No pude generar una respuesta.';
          this.messages.set([...this.messages(), { sender: 'bot', text: respuesta }]);
          this.cargando = false;
        },
        error: () => {
          this.messages.set([
            ...this.messages(),
            { sender: 'bot', text: 'No pude comunicarme con el asistente en este momento.' },
          ]);
          this.cargando = false;
        },
      });
  }
}
