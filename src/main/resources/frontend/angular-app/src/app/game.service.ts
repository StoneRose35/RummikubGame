import { Injectable } from '@angular/core';
import { Observable, of, timer, Subject } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Figure } from './figure';
import { HttpClient } from '@angular/common/http';
import { backendPort, environment } from './../environments/environment';


export interface Game {
  message: String;
  tableFigures: Array<Array<Figure>>;
  error: String; 

}

export interface GameOverview {
  players: Array<String>;
  name: String;
  state: String;
}

export interface GameState {
  tableFigures: Array<Array<Figure>>;
  shelfFigures: Array<Figure>;
  roundNr: number;
  accepted: boolean;
}

export interface Player {
  name: String;
  active: boolean;
  finalScore: number;
}

export interface Response {
  message: String;
  error: String;
}

export interface ResponsePlayer {
  message: String;
  error: String;
  player: Player;
}

@Injectable({
  providedIn: 'root'
})
export class GameService {

  p: Player;
  gameId: String;
  activityChangedSubject: Subject<boolean>;
  roundNr: number;
  url: String;

  constructor(private http: HttpClient) { 
    this.activityChangedSubject=new Subject<boolean>();
    this.p = {name: null, active: false, finalScore: null};
    this.url = window.location.protocol + "//" + window.location.hostname + ":" + backendPort;
  }

  public getGames(): Observable<Array<GameOverview>>
  {
      return this.http.get<Array<GameOverview>>(this.url + "/games");
  }

  public pollGames(): Observable<Array<GameOverview>>
  {
    return timer(1,500).pipe(switchMap(() => this.http.get<Array<GameOverview>>(this.url + "/games")));
  }

  public startGame(gameId: String): Observable<Game>
  {
    let data = {message: "Game Started", tableFigures: [[{color: {name: "red", rgb: "#ff0000", code: 0}, instance: 0, number: 12}]], error: ""}
    return of(data);
  }

  public registerPlayer(playerName: String, gameName: String): Observable<ResponsePlayer>
  {
    return this.http.get<ResponsePlayer>(this.url + "/registerPlayer",{params: {name: playerName.toString(),gameId: gameName.toString() },withCredentials: true})
  }

  public shelfFigures(): Observable<Array<Figure>>
  {
    return this.http.get<Array<Figure>>(this.url + "/shelfFigures",{withCredentials: true});
  }

  public initGame(gameName: String,nrAiPlayers: number): Observable<Response> 
  {
    this.gameId = gameName;
    return this.http.get<Response>(this.url + "/newgame",{params: {name: gameName.toString(),nrAiPlayers: nrAiPlayers.toString()}});
  }

  public pollPlayers(): Observable<Array<Player>> 
  {
    const act_old = this.p.active;
    return timer(1,500).pipe(switchMap(() => this.http.get<Array<Player>>(this.url + "/players",{withCredentials: true})));
  }

  activityChanged(): Observable<boolean> {
    return this.activityChangedSubject;
  }

  getTable(): Observable<Array<Array<Figure>>>
  {  
    return this.http.get<Array<Array<Figure>>>(this.url + "/tableFigures",{withCredentials: true});
  }

  public drawFigure(): Observable<Figure>
  {
    return this.http.get<Figure>(this.url + "/draw",{withCredentials: true});
  }

  public submitMove(stateOld: GameState): Observable<GameState>
  {
    return this.http.post<GameState>(this.url + "/submitMove",stateOld,{withCredentials: true});
  }

  public dispose(): Observable<Response>
  {
    return this.http.get<Response>(this.url + "/dispose",{withCredentials: true});
  }

}
