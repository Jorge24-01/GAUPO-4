import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideTransloco } from '@jsverse/transloco';

import { Familia } from './familia';
import { TranslocoHttpLoader } from '../../transloco-loader';

describe('Familia', () => {
  let component: Familia;
  let fixture: ComponentFixture<Familia>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Familia],
      providers: [
        provideTransloco({
          config: { availableLangs: ['es-419', 'en-US'], defaultLang: 'es-419' },
          loader: TranslocoHttpLoader,
        }),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Familia);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
