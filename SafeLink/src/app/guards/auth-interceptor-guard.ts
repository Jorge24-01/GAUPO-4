import { HttpInterceptorFn } from '@angular/common/http';
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { map } from 'rxjs';
import { Auth } from '../services/auth';

export const authGuard: CanActivateFn = (_route, state) => {
  const auth = inject(Auth);
  const router = inject(Router);

  if (auth.getToken()) {
    auth.cargarUsuarioActual().subscribe();
    return true;
  }

  return router.createUrlTree(['/login'], {
    queryParams: { returnUrl: state.url },
  });
};

export const adminGuard: CanActivateFn = (_route, state) => {
  const auth = inject(Auth);
  const router = inject(Router);

  if (!auth.getToken()) {
    return router.createUrlTree(['/login'], {
      queryParams: { returnUrl: state.url },
    });
  }

  if (auth.isAdmin()) {
    return true;
  }

  return auth.cargarUsuarioActual(true).pipe(
    map(() => auth.isAdmin() ? true : router.createUrlTree(['/inicio']))
  );
};

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem(Auth.TOKEN_KEY);
  const esExterno = !req.url.includes('https://safelink-puvx.onrender.com');
  const idioma = localStorage.getItem('safelink-lang') ?? 'es-419';

  if (token && !esExterno) {
    const authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}`, 'Accept-Language': idioma },
    });
    return next(authReq);
  }

  return next(req.clone({ setHeaders: { 'Accept-Language': idioma } }));
};
