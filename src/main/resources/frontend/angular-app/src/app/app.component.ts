import { Component, OnInit } from '@angular/core';
import { GameService } from './game.service';
import { MatDialog } from '@angular/material/dialog';
import { NewPlayerDialogComponent } from './new-player-dialog/new-player-dialog.component';
import { Router } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  title = 'RummikubWeb';

  SVGFile: SafeUrl;

  constructor(public gs: GameService,private dialog: MatDialog, protected sanitizer: DomSanitizer, private router: Router)
  {

  }

  ngOnInit(): void 
  {
    this.gs.reconnect().subscribe(r => {
      if (r !== null)
      {
        this.gs.p = r.player;
        this.gs.gameId = r.gameId;
        this.gs.gameName = r.gameName;
        this.gs.token = r.token;
        this.placeAvatar();
      }
      else
      {
        this.gs.p = null;
        this.gs.gameId = null;
        this.gs.gameName = null;
      }
    });

  }

  addPlayer()
  {
    const dialogRef = this.dialog.open(NewPlayerDialogComponent);
    dialogRef.beforeClosed().subscribe(s => {
      this.gs.addPlayer(s).subscribe(r => {
        this.gs.p = r.player;
        this.placeAvatar();
      })
    }); 
  }

  placeAvatar(): void
  {
    let avatarString = this.gs.p.avatar;
    avatarString = avatarString.replace(/height="([0-9]*)"/,'height="40"');
    avatarString = avatarString.replace(/width="([0-9]*)"/,'height="40"');
    this.SVGFile = this.sanitizer.bypassSecurityTrustHtml(avatarString);
  } 

  leaveApplication(): void
  {
    this.gs.leave().subscribe(r => {
      this.gs.gameId = null;
      this.gs.gameName = null;
      this.gs.p = null;
      this.router.navigateByUrl("/overview");
    });
  }

}
