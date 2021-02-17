import { Component, OnInit, Inject } from '@angular/core';
import { GameService, Player } from './../game.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-player-ready-dialog',
  templateUrl: './player-ready-dialog.component.html',
  styleUrls: ['./player-ready-dialog.component.scss']
})
export class PlayerReadyDialogComponent implements OnInit {

  private gameService: GameService;
  players: Array<Player>;


  constructor(public dialogRef: MatDialogRef<PlayerReadyDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: GameService,protected sanitizer: DomSanitizer)
    {
      this.gameService = data;
    }


  ngOnInit(): void {
    this.gameService.watchPlayers().subscribe(p => {
      this.players = p;
      this.players.forEach( p2 => {
        p2.avatarSvg = this.sanitizer.bypassSecurityTrustHtml(p2.avatar);
      });
      if(this.players.filter(p => p.ready===false).length===0)
      {
        this.dialogRef.close();
      }
    });

    this.gameService.getPlayers().subscribe(p => {
      this.players = p;
      this.players.forEach( p2 => {
        p2.avatarSvg = this.sanitizer.bypassSecurityTrustHtml(p2.avatar);
      });

      if(this.players.filter(p => p.ready===false).length===0)
      {
        this.dialogRef.close();
      }
    });

  }

  setReady()
  {
    this.gameService.setReady().subscribe(r => {
      if(r.error !== null)
      {
        console.log(r.error);
      }
    });
  }

}
