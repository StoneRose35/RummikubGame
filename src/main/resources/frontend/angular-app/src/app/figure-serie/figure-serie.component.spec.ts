import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FigureSerieComponent } from './figure-serie.component';

describe('FigureSerieComponent', () => {
  let component: FigureSerieComponent;
  let fixture: ComponentFixture<FigureSerieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FigureSerieComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FigureSerieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
