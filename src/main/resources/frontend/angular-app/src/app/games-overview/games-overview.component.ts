import { Component, OnInit, OnDestroy } from '@angular/core';
import { GameService, GameOverview } from './../game.service';
import { NewPlayerDialogComponent } from './../new-player-dialog/new-player-dialog.component';
import { MatSnackBar,MatSnackBarConfig } from '@angular/material/snack-bar';
import {MatDialog} from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-games-overview',
  templateUrl: './games-overview.component.html',
  styleUrls: ['./games-overview.component.scss']
})
export class GamesOverviewComponent implements OnInit, OnDestroy {

  games: Array<GameOverview>;
  aiPlayers;
  gameName: String;
  pollGamesSubscription;

  constructor(private snackBar: MatSnackBar,
              private sbConfig: MatSnackBarConfig,
              private gs: GameService,
              private dialog: MatDialog,
              private router: Router){ }

  ngOnDestroy(): void {
    if (this.pollGamesSubscription !== null)
    {
      this.pollGamesSubscription.unsubscribe();
      this.pollGamesSubscription=null;
    }
  }

  ngOnInit(): void {
    if (this.pollGamesSubscription == null)
    {
      this.pollGamesSubscription = this.gs.pollGames().subscribe(games => this.games=games);
    }
  }


  registerGame()
  {
    this.gs.initGame(this.gameName,this.aiPlayers).subscribe(r => {
      if (r.error !== null)
      {
        this.snackBar.open(`game initialization failed: ${r.error}`,null,this.sbConfig);
      }
    });
  }

  joinGame(gameName: String)
  {
    const dialogRef = this.dialog.open(NewPlayerDialogComponent);
        dialogRef.afterClosed().subscribe(s => {
          this.gs.registerPlayer(s,gameName).subscribe(r => {
            if (r.error != null)
            {
                this.snackBar.open(`Player registration failed: ${r.error}`,null,this.sbConfig);
            }
            else
            {
                this.gs.p = r.player;
                this.gs.token = r.token;
                this.router.navigateByUrl("/game-management");
            }
          });
        });
  }

}
