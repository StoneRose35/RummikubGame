import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayerReadyDialogComponent } from './player-ready-dialog.component';

describe('PlayerReadyDialogComponent', () => {
  let component: PlayerReadyDialogComponent;
  let fixture: ComponentFixture<PlayerReadyDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlayerReadyDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlayerReadyDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
