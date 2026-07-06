import { TestBed } from '@angular/core/testing';

import { Safelink } from './safelink';

describe('Safelink', () => {
  let service: Safelink;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Safelink);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
