import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmptyFigureSerieComponent } from './empty-figure-serie.component';

describe('EmptyFigureSerieComponent', () => {
  let component: EmptyFigureSerieComponent;
  let fixture: ComponentFixture<EmptyFigureSerieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmptyFigureSerieComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmptyFigureSerieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
