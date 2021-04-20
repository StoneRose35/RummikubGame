import { Component } from '@angular/core';
import { GameService } from './game.service';
import { MatDialog } from '@angular/material/dialog';
import { NewPlayerDialogComponent } from './new-player-dialog/new-player-dialog.component';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  title = 'RummikubWeb';

  SVGFile: SafeUrl;

  constructor(public gs: GameService,private dialog: MatDialog, protected sanitizer: DomSanitizer)
  {

  }

  addPlayer()
  {
    const dialogRef = this.dialog.open(NewPlayerDialogComponent);
    dialogRef.beforeClosed().subscribe(s => {
      this.gs.addPlayer(s).subscribe(r => {
        console.log(r);
        this.gs.p = r.player;
        this.SVGFile = this.sanitizer.bypassSecurityTrustHtml(this.gs.p.avatar);
      })
    }); 
  }

}
