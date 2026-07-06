import { Routes } from '@angular/router';
import { Inicio } from './pages/inicio/inicio';
import { Login } from './pages/login/login';
import { Registro } from './pages/registro/registro';
import { Mapa } from './pages/mapa/mapa';
import { Kit } from './pages/kit/kit';
import { Familia } from './pages/familia/familia';
import { Ayuda } from './pages/ayuda/ayuda';
import { AdminPage } from './pages/admin/admin';
import { adminGuard, authGuard } from './guards/auth-interceptor-guard';

export const routes: Routes = [
  { path: '', component: Inicio },
  { path: 'inicio', redirectTo: '', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'registro', component: Registro },
  { path: 'mapa', component: Mapa, canActivate: [authGuard] },
  { path: 'kit', component: Kit, canActivate: [authGuard] },
  { path: 'familia', component: Familia, canActivate: [authGuard] },
  { path: 'ayuda', component: Ayuda },
  { path: 'admin', component: AdminPage, canActivate: [adminGuard] },
  { path: '**', redirectTo: '' },
];
