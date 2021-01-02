import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { GameManagementComponent } from './game-management.component';
import {MatSnackBarModule, MatSnackBarConfig} from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';
import {DragDropModule} from '@angular/cdk/drag-drop';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { GameService } from '../game.service';


describe('GameManagementComponent', () => {
  let component: GameManagementComponent;
  let fixture: ComponentFixture<GameManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameManagementComponent ],
      imports: [MatSnackBarModule, MatDialogModule, DragDropModule, HttpClientModule, BrowserAnimationsModule],
      providers: [MatSnackBarConfig]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
