import { TestBed } from '@angular/core/testing';
import { authInterceptor } from './auth-interceptor-guard';

describe('authInterceptor', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(authInterceptor).toBeTruthy();
  });
});
