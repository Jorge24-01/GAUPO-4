import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideTransloco } from '@jsverse/transloco';

import { Kit } from './kit';
import { TranslocoHttpLoader } from '../../transloco-loader';

describe('Kit', () => {
  let component: Kit;
  let fixture: ComponentFixture<Kit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Kit],
      providers: [
        provideTransloco({
          config: { availableLangs: ['es-419', 'en-US'], defaultLang: 'es-419' },
          loader: TranslocoHttpLoader,
        }),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Kit);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
