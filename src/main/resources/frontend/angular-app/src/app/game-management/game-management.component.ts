import { Component, OnInit, ViewContainerRef, OnDestroy, DoCheck } from '@angular/core';
import {MatSnackBar,MatSnackBarConfig} from '@angular/material/snack-bar';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {Figure} from './../figure';
import { GameService, Player } from './../game.service';
import { JokerProcessor } from './../joker-processor'
import { Router } from '@angular/router';
import {Overlay, OverlayConfig } from '@angular/cdk/overlay';
import { ComponentPortal } from '@angular/cdk/portal';
import { WinnerScreenComponent } from '../winner-screen/winner-screen.component';

@Component({
  selector: 'app-game-management',
  templateUrl: './game-management.component.html',
  styleUrls: ['./game-management.component.scss']
})
export class GameManagementComponent implements OnInit, OnDestroy {

  tableFigures: Array<Array<Figure>>;
  voidList: Array<Figure>;
  stackFiguresUpper: Array<Figure>;
  stackFiguresLower: Array<Figure>;
  players: Array<Player>;
  playing: Boolean;
  activePlayer: Player;
  stackFiguresOld: Array<Figure>;
  tableFiguresOld: Array<Array<Figure>>;
  playerPollSubscription: any;
  tablePollSubscription: any;
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

    this.players=[];
    this.playing=false;
  }



  ngOnInit(): void {

    this.activePlayer = this.gs.p;
    this.snackBar.open(`Entering Game ${this.gs.gameId}`,null,this.sbConfig);
    this.stackFiguresUpper = [];
    this.stackFiguresLower = [];
    this.tableFigures=[];
    this.voidList=[new Figure(null,5,0,0,0)];
    var f_obj: Figure;
    var figArray: Array<Figure>;
    this.gs.getTable().subscribe(t => this.convertTableFigures(t));
    this.gs.shelfFigures().subscribe(f => {
      figArray =[];
      f.forEach(fig => {
        f_obj = new Figure(fig.colorcode,fig.instance,fig.number,fig.shelfNr,fig.position);
        figArray.push(f_obj);
      });
      this.placeOnShelves(figArray);
      this.onTurn();
    });
    if (this.playerPollSubscription == null)
    {
      this.playerPollSubscription = this.gs.watchPlayers().subscribe(ps => this.handlePlayerUpdate(ps),error => {console.log("something bad happened: " + error)});
    }
    this.gs.getPlayers().subscribe(ps => this.handlePlayerUpdate(ps));
  }

  ngOnDestroy()
  {
    if (this.playerPollSubscription != null) 
    {
      this.playerPollSubscription.unsubscribe();
      this.playerPollSubscription=null;
    }
    if (this.tablePollSubscription != null)
    {
      this.tablePollSubscription.unsubscribe();
      this.tablePollSubscription=null;
    }
    this.gs.p=null;
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
    this.gs.drawFigure().subscribe(fig => this.placeSingleOnShelves(fig));
  }

  handlePlayerUpdate(ps: Array<Player>)
  {
    let prevPlayer = this.players.find(p => p.active===true);
    let actualPlayer = ps.find(p2 => p2.active===true);

    this.players=ps;
    this.gs.p.active = ps.filter(p => p.name === this.gs.p.name)[0].active;
    this.gs.p.finalScore = ps.filter(p => p.name === this.gs.p.name)[0].finalScore;
    if (this.gs.p.finalScore !== null)
    {
      this.showWinnerScreen();
    }
    if (this.playing===true && this.gs.p.active===false)
    {
      // switch from active to passive
      this.playing=this.gs.p.active;
    }
    else if (this.playing===false && this.gs.p.active===true)
    {
      // switch from passive to active
      this.gs.getTable().subscribe(t => {
        this.convertTableFigures(t);
        this.onTurn();
        this.playing=this.gs.p.active;
      });
    }
    else if (prevPlayer!= null && prevPlayer.name !== actualPlayer.name)
    // switch between opponents
    {
      this.gs.getTable().subscribe(t => this.convertTableFigures(t));
    }
    else
    {
      this.playing = this.gs.p.active;
    }
  }

  convertTableFigures(t: Array<Array<Figure>>): void
  {
    var figSeries=[];
    var f_obj: Figure;
    t.forEach(s => {
      var figArray =[];
      s.forEach(tf => {
      f_obj = new Figure(tf.colorcode,tf.instance,tf.number,tf.shelfNr,tf.position);
      figArray.push(f_obj);
      });
      figSeries.push(figArray);
    });
    this.tableFigures=figSeries;
  }

  submitMove() {
    this.tableFigures = this.tableFigures.filter(f => f.length > 0);
    this.tableFigures.forEach(ts => ts.forEach(tf => {tf.position=null; tf.shelfNr=null;}));
    var allShelfFigures= this.stackFiguresUpper.concat(this.stackFiguresLower);
    const gameState={tableFigures: this.tableFigures, shelfFigures: allShelfFigures, accepted: false, roundNr: 13 };

    this.gs.submitMove(gameState).subscribe(r => {
      if (r.accepted == false) // game state submitted is invalid
      {
        this.snackBar.open("Invalid Move, resetting",null,this.sbConfig);
        this.resetMove();
      }
      var figArray: Array<Figure>;
      var f_obj: Figure;
      figArray =[];
      r.shelfFigures.forEach(fig => {
        f_obj = new Figure(fig.colorcode,fig.instance,fig.number,fig.shelfNr,fig.position);
        figArray.push(f_obj);
      });
      this.stackFiguresUpper = figArray.filter(f => f.shelfNr===0);
      this.stackFiguresLower = figArray.filter(f => f.shelfNr===1);
      this.convertTableFigures(r.tableFigures);
    });
  }

  resetMove() {
    this.stackFiguresUpper=[];
    this.stackFiguresLower=[];
    this.tableFigures=[];
    this.stackFiguresOld.filter(f => f.shelfNr===0).forEach(f => {
      this.stackFiguresUpper.push(f);
    });
    this.stackFiguresOld.filter(f => f.shelfNr===1).forEach(f => {
      this.stackFiguresLower.push(f);
    });
    this.jp.reset(this.stackFiguresUpper);
    this.jp.reset(this.stackFiguresLower);
    this.tableFiguresOld.forEach(tf => {
      this.tableFigures.push([]);
      tf.forEach(f => {
        this.tableFigures[this.tableFigures.length-1].push(f);
      });
      this.jp.process(tf);
    });
    this.cannotDraw=false;
    this.cannotSubmit=true;
  }

  onTurn() {

    this.playing=true;
    this.stackFiguresOld=[];
    this.tableFiguresOld=[];
    this.stackFiguresUpper.forEach(f => {
      this.stackFiguresOld.push(f);
    });
    this.stackFiguresLower.forEach(f => {
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

  dropupper(event: CdkDragDrop<Figure[]>) {
    this.drop(event, 0);
  }

  droplower(event: CdkDragDrop<Figure[]>) {
    this.drop(event, 1);
  }

  drop(event: CdkDragDrop<Figure[]>,shelfIndex: number)
  {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);

    } else {
      transferArrayItem(event.previousContainer.data,
                        event.container.data,
                        event.previousIndex,
                        event.currentIndex);
    }
    if (shelfIndex===0)
    {
      this.stackFiguresUpper.forEach((f,i) => {f.position = i; f.shelfNr=shelfIndex;});
      this.jp.reset(this.stackFiguresUpper);
    }
    else{
      this.stackFiguresLower.forEach((f,i) => {f.position = i; f.shelfNr=shelfIndex;});
      this.jp.reset(this.stackFiguresLower);
    }
    var allShelfFigures=this.stackFiguresUpper.concat(this.stackFiguresLower);
    this.gs.updateShelf(allShelfFigures).subscribe();
  }

  dropNewSeries(event: CdkDragDrop<Figure[]>) {
    this.tableFigures.push([]);
    transferArrayItem(event.previousContainer.data,
                      this.tableFigures[this.tableFigures.length-1],
                      event.previousIndex,
                      0);
    this.tableFigures = this.tableFigures.filter(f => f.length > 0);
    this.enableSubmit();
  }

  fdCallback()
  {
    this.tableFigures = this.tableFigures.filter(f => f.length > 0);
    this.enableSubmit();
  }

  enableSubmit()
  {
    this.cannotDraw=true;
    this.cannotSubmit=false;
  }

  placeOnShelves(figures: Array<Figure>)
  {
    this.stackFiguresUpper = figures.filter(f => f.shelfNr===0).sort((f1,f2) => f1.position - f2.position);
    this.stackFiguresLower = figures.filter(f => f.shelfNr===1).sort((f1,f2) => f1.position - f2.position);
  }

  placeSingleOnShelves(figure: Figure)
  {
    if (figure.shelfNr===0)
    {
      this.stackFiguresUpper.push(new Figure(figure.colorcode,figure.instance,figure.number,figure.shelfNr,figure.position));
    }
    else
    {
      this.stackFiguresLower.push(new Figure(figure.colorcode,figure.instance,figure.number,figure.shelfNr,figure.position));
    }
  }
}
