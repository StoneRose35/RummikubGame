import { ComponentFixture, TestBed } from '@angular/core/testing';
import {MatSnackBarModule, MatSnackBarConfig} from '@angular/material/snack-bar';
import { GamesOverviewComponent } from './games-overview.component';
import { HttpClientModule } from '@angular/common/http';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';

describe('GamesOverviewComponent', () => {
  let component: GamesOverviewComponent;
  let fixture: ComponentFixture<GamesOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GamesOverviewComponent ],
      imports: [MatSnackBarModule, HttpClientModule, MatDialogModule, RouterTestingModule],
      providers: [MatSnackBarConfig, MatDialog]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GamesOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
