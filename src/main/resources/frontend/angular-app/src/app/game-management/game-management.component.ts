import { Component, OnInit, ViewContainerRef, OnDestroy } from '@angular/core';
import {MatSnackBar,MatSnackBarConfig} from '@angular/material/snack-bar';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {Figure} from './../figure';
import { GameService, Player } from './../game.service';
import { JokerProcessor } from './../joker-processor'
import { Router } from '@angular/router';
import {Overlay, OverlayConfig } from '@angular/cdk/overlay';
import { ComponentPortal } from '@angular/cdk/portal';
import { WinnerScreenComponent } from '../winner-screen/winner-screen.component';
import { ThrowStmt } from '@angular/compiler';

@Component({
  selector: 'app-game-management',
  templateUrl: './game-management.component.html',
  styleUrls: ['./game-management.component.scss']
})
export class GameManagementComponent implements OnInit, OnDestroy {

  tableFigures: Array<Array<Figure>>;
  stackFigures: Array<Figure>;
  players: Array<Player>;
  message: String;
  playing: Boolean;
  gameState: String;
  activePlayer: Player;
  stackFiguresOld: Array<Figure>;
  tableFiguresOld: Array<Array<Figure>>;
  playerPollSubscription: any;
  tablePollSubscription: any;
  activityChangeSubscription: any;
  cannotDraw: boolean;
  cannotSubmit: boolean;
 

  constructor(private snackBar: MatSnackBar
              ,private sbConfig: MatSnackBarConfig
              ,public gs: GameService
              ,public jp: JokerProcessor
              ,private router: Router
              ,private overlay: Overlay
              ,public viewContainerRef: ViewContainerRef
              ) { 
    this.sbConfig.duration=2000;
    this.stackFigures = [];
    this.players=[];
    this.gameState = "New Game";
  }

  ngOnInit(): void {

    this.activePlayer = this.gs.p;
    this.gameState="Start Game";
    this.snackBar.open(`New Game ${this.gs.gameId} Initialized`,null,this.sbConfig);
    this.message=`Running Game ${this.gs.gameId}`;
    this.stackFigures = [];
    this.tableFigures=[];
    this.gs.getTable().subscribe(t => {
      this.tableFigures=t;
    });
    this.gs.shelfFigures().subscribe(f => {
      this.stackFigures=f;
      this.onTurn();
    });
    this.cannotSubmit
    if (this.playerPollSubscription == null)
    {
      this.playerPollSubscription = this.gs.pollPlayers().subscribe(ps => {
        this.players=ps;
        this.gs.p.active = ps.filter(p => p.name === this.gs.p.name)[0].active;
        this.gs.p.finalScore = ps.filter(p => p.name === this.gs.p.name)[0].finalScore;
        if (this.gs.p.finalScore !== null)
        {
          this.showWinnerScreen();
        }
        if (this.playing===true && this.gs.p.active===false)
        {
          // switch from active to inactive
          this.playing=this.gs.p.active;
        }
        else if (this.playing===false && this.gs.p.active===true)
        {
          // switch from passive to active
          this.gs.getTable().subscribe(t => {
            this.tableFigures=t; 
            this.onTurn();
            this.playing=this.gs.p.active;
          });
        }
        else
        {
          this.playing = this.gs.p.active;
        }
      });
    }
  }

  ngOnDestroy()
  {
    if (this.playerPollSubscription !== null) 
    {
      this.playerPollSubscription.unsubscribe();
      this.playerPollSubscription=null;
    }
  }

  showWinnerScreen()
  {
    this.playerPollSubscription.unsubscribe();
    this.playerPollSubscription=null;
    
    let config = new OverlayConfig();

    config.positionStrategy = this.overlay.position()
        .global()
        .centerHorizontally()
        .centerVertically();


    config.hasBackdrop = true;

    let overlayRef = this.overlay.create(config);

    overlayRef.backdropClick().subscribe(() => {
      this.gs.dispose().subscribe();
      overlayRef.dispose();
      this.router.navigateByUrl("/overview");
    });
  
    let portal = new ComponentPortal(WinnerScreenComponent, this.viewContainerRef);
    const componentRef = overlayRef.attach(portal);
    componentRef.instance.players = this.players.sort((p1,p2) => p1.finalScore - p2.finalScore);
  }

  drawFigure() {
    this.gs.drawFigure().subscribe(fig => this.stackFigures.push(fig));
  }

  submitMove() {
    this.tableFigures = this.tableFigures.filter(f => f.length > 0);
    const gameState={tableFigures: this.tableFigures, shelfFigures: this.stackFigures, accepted: false, roundNr: 13 };

    this.gs.submitMove(gameState).subscribe(r => {
      if (r.accepted == false) // game state submitted is invalid
      {
        this.snackBar.open("Invalid Move, resetting",null,this.sbConfig);
        this.resetMove();
      }
      this.stackFigures = r.shelfFigures;
      this.tableFigures = r.tableFigures;
    });
  }

  resetMove() {
    this.stackFigures=[];
    this.tableFigures=[];
    this.stackFiguresOld.forEach(f => {
      this.stackFigures.push(f);
    });
    this.jp.reset(this.stackFigures);
    this.tableFiguresOld.forEach(tf => {
      this.tableFigures.push([]);
      tf.forEach(f => {
        this.tableFigures[this.tableFigures.length-1].push(f);
      });
      this.jp.process(tf);
    });

  }

  onTurn() {

    this.playing=true;
    this.stackFiguresOld=[];
    this.tableFiguresOld=[];
    this.stackFigures.forEach(f => {
      this.stackFiguresOld.push(f);
    });
    this.tableFigures.forEach(tf => {
      this.tableFiguresOld.push([]);
      tf.forEach(f => {
        this.tableFiguresOld[this.tableFiguresOld.length-1].push(f);
      });
    });
    this.cannotDraw=false;
    this.cannotSubmit=true;

  }

  drop(event: CdkDragDrop<Figure[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
                        event.container.data,
                        event.previousIndex,
                        event.currentIndex);
      this.jp.reset(event.container.data);
    }
  }

  dropNewSeries(event: CdkDragDrop<Figure[]>) {
    this.tableFigures.push([]);
    transferArrayItem(event.previousContainer.data,
                      this.tableFigures[this.tableFigures.length-1],
                      event.previousIndex,
                      0);
    this.enableSubmit();
  }

  enableSubmit()
  {
    this.cannotDraw=true;
    this.cannotSubmit=false;
  }
}
