import { TestBed } from '@angular/core/testing';

import { RefugioService } from './refugio';

describe('RefugioService', () => {
  let service: RefugioService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RefugioService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
