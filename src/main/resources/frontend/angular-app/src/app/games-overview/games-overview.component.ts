import { Component, OnInit, OnDestroy } from '@angular/core';
import { GameService, GameOverview } from './../game.service';
import { NewPlayerDialogComponent } from './../new-player-dialog/new-player-dialog.component';
import { MatSnackBar,MatSnackBarConfig } from '@angular/material/snack-bar';
import {MatDialog} from '@angular/material/dialog';
import { Router } from '@angular/router';
import { PlayerReadyDialogComponent } from '../player-ready-dialog/player-ready-dialog.component';

@Component({
  selector: 'app-games-overview',
  templateUrl: './games-overview.component.html',
  styleUrls: ['./games-overview.component.scss']
})
export class GamesOverviewComponent implements OnInit, OnDestroy {

  games: Array<GameOverview>;
  aiPlayers;
  maxDuration;
  gameName: String;
  pollGamesSubscription;

  constructor(private snackBar: MatSnackBar,
              private sbConfig: MatSnackBarConfig,
              private gs: GameService,
              private dialog: MatDialog,
              private router: Router){ this.maxDuration="0"; }

  ngOnDestroy(): void {
    if (this.pollGamesSubscription !== null)
    {
      this.pollGamesSubscription.unsubscribe();
      this.pollGamesSubscription=null;
    }
  }

  ngOnInit(): void {
    this.gs.getGames().subscribe(g =>{
      this.games = g;
      if (this.pollGamesSubscription == null)
      {
        this.pollGamesSubscription = this.gs.watchGames().subscribe(games => this.games=games);
      }
    });
    this.gs.reconnect().subscribe(r => {
      if (r !== null)
      {
        this.gs.p = r.player;
        this.gs.gameId = r.gameId;
        this.gs.gameName = r.gameName;
        this.gs.token = r.token;
      }
      else
      {
        this.gs.p = null;
        this.gs.gameId = null;
        this.gs.gameName = null;
      }
    });
  }

  
  registerGame()
  {
    if (this.aiPlayers==null)
    {
      this.snackBar.open(`Define the number of computer players`,null,this.sbConfig);
    }
    else
    {
      this.gs.initGame(this.gameName,this.aiPlayers,this.maxDuration).subscribe(r => {
        if (r.error !== null)
        {
          this.gs.gameId=null;
          this.snackBar.open(`game initialization failed: ${r.error}`,null,this.sbConfig);
        }
        else {
          this.gs.gameId=r.gameId;
        }
      });
    }
  }

  joinGame(gameId: String)
  {
    const dialogRef = this.dialog.open(NewPlayerDialogComponent);
        dialogRef.afterClosed().subscribe(s => {
          this.gs.registerPlayer(s,gameId).subscribe(r => {
            if (r.error != null)
            {
                this.snackBar.open(`Player registration failed: ${r.error}`,null,this.sbConfig);
            }
            else
            {
                this.gs.p = r.player;
                this.gs.gameId = r.gameId;
                this.gs.gameName = r.gameName;
                this.gs.token = r.token;
                const dialogRef2 = this.dialog.open(PlayerReadyDialogComponent,{width: '330px', height: '400px',data: this.gs});
                dialogRef2.afterClosed().subscribe(s => {
                  this.router.navigateByUrl("/game-management");
                });
            }
          });
        });
  }

}
