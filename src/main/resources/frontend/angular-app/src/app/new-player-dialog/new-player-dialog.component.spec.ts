import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { NewPlayerDialogComponent } from './new-player-dialog.component';

describe('NewPlayerDialogComponent', () => {
  let component: NewPlayerDialogComponent;
  let fixture: ComponentFixture<NewPlayerDialogComponent>;
 
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewPlayerDialogComponent ],
      providers: [{ provide: MatDialogRef, useValue: {} },{
        provide: MAT_DIALOG_DATA,
        useValue: "Fritz"
      }]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewPlayerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
