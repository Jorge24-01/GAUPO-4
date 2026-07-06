import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { CommonModule } from '@angular/common';
import { TranslocoModule, TranslocoService, provideTranslocoScope } from '@jsverse/transloco';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Auth } from '../../services/auth';
import { ThemeService } from '../../services/theme';
@Component({
  selector: 'app-menu',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, MatToolbarModule, MatButtonModule, MatIconModule, MatMenuModule, TranslocoModule],
  providers: [provideTranslocoScope('menu')],
  templateUrl: './menu.html',
  styleUrl: './menu.scss',
})
export class Menu implements OnInit {
  auth = inject(Auth);
  theme = inject(ThemeService);
  private transloco = inject(TranslocoService);
  private liveAnnouncer = inject(LiveAnnouncer);

  ngOnInit(): void {
    const idiomaGuardado = localStorage.getItem('safelink-lang');
    if (idiomaGuardado === 'es-419' || idiomaGuardado === 'en-US') {
      this.transloco.setActiveLang(idiomaGuardado);
    }
  }

  idiomaActual(): string {
    return this.transloco.getActiveLang();
  }

  cambiarIdioma(): void {
    const nuevoIdioma = this.idiomaActual() === 'es-419' ? 'en-US' : 'es-419';
    // El localStorage se actualiza antes de setActiveLang porque este ultimo dispara
    // sincronamente los suscriptores de langChanges$ (ej. recargas de datos del backend
    // via HTTP), y el interceptor lee el idioma desde localStorage al armar el header
    // Accept-Language: si se hiciera despues, esas peticiones saldrian con el idioma anterior.
    localStorage.setItem('safelink-lang', nuevoIdioma);
    this.transloco.setActiveLang(nuevoIdioma);
    this.transloco.selectTranslate('idiomaAnunciado', {}, 'menu').subscribe(texto => {
      this.liveAnnouncer.announce(texto);
    });
  }
}
