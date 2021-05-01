import { Injectable } from '@angular/core';
import { SafeHtml } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Figure } from './figure';
import { HttpClient } from '@angular/common/http';
import { backendPort } from './../environments/environment';
import { RxStompService } from '@stomp/ng2-stompjs';


export interface Game {
  message: String;
  tableFigures: Array<Array<Figure>>;
  error: String; 

}

export interface GameOverview {
  players: Array<String>;
  name: String;
  state: String;
  gameId: String;
  started: boolean;
}

export interface GameState {
  tableFigures: Array<Array<Figure>>;
  shelfFigures: Array<Figure>;
  roundNr: number;
  accepted: boolean;
}

export interface Player {
  avatar: string;
  avatarSvg: SafeHtml;
  name: String;
  active: boolean;
  finalScore: number;
  timeRemaining: number;
  ready: boolean;
  cards: number;
}

export interface Response {
  message: String;
  error: String;
}

export interface ResponsePlayer {
  message: String;
  error: String;
  player: Player;
  token: String;
  gameName: String;
  gameId: String;
}

export interface ResponseNewGame {
  message: String;
  error: String;
  gameId: String;
  game: GameOverview;
}

@Injectable({
  providedIn: 'root'
})
export class GameService {

  p: Player;
  gameId: String;
  gameName: String;
  url: String;
  token: String;

  constructor(private http: HttpClient, private stompClient: RxStompService) { 
    this.p = null;
    this.url = window.location.protocol + "//" + window.location.hostname + ":" + backendPort;
  }

  public getGames(): Observable<Array<GameOverview>>
  {
      return this.http.get<Array<GameOverview>>(this.url + "/games",{withCredentials: true});
  }

  public watchGames(): Observable<Array<GameOverview>>
  {
    return this.stompClient.watch("/topic/games").pipe(map(msg => {return JSON.parse(msg.body);}));
  }

  public registerPlayer(playerName: String, gameId: String): Observable<ResponsePlayer>
  {
    return this.http.get<ResponsePlayer>(this.url + "/registerPlayer",{params: {name: playerName.toString(),gameId: gameId.toString() },withCredentials: true});
  }

  public shelfFigures(): Observable<Array<Figure>>
  {
    return this.http.get<Array<Figure>>(this.url + "/shelfFigures",{withCredentials: true});
  }

  public initGame(gameName: String,nrAiPlayers: number,maxDuration: number): Observable<ResponseNewGame> 
  {
    this.gameId = gameName;
    return this.http.get<ResponseNewGame>(this.url + "/newgame",{params: {name: gameName.toString(),nrAiPlayers: nrAiPlayers.toString(), maxDuration: maxDuration.toString()},withCredentials: true});
  }

  public reconnect(): Observable<ResponsePlayer>
  {
    return this.http.get<ResponsePlayer>(this.url + "/reconnect",{withCredentials: true});
  }

  public watchPlayers(): Observable<Array<Player>> 
  {
    return this.stompClient.watch("/topic/game" + this.gameId + "/players").pipe(map(msg => {return JSON.parse(msg.body);}));
  }

  public getPlayers(): Observable<Array<Player>>
  {
    return this.http.get<Array<Player>>(this.url + "/players",{withCredentials: true});
  }

  getTable(): Observable<Array<Array<Figure>>>
  {  
    return this.http.get<Array<Array<Figure>>>(this.url + "/tableFigures",{withCredentials: true});
  }

  updateShelf(figures: Array<Figure>): Observable<Response> {
    return this.http.post<Response>(this.url + "/updateShelf", figures, {withCredentials: true});
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

  public setReady(): Observable<Response>
  {
    return this.http.get<Response>(this.url + "/ready", {withCredentials: true});
  }

  public watchPrivate(): Observable<Object>
  {
    return this.stompClient.watch("/topic/player" + this.token + "/common").pipe(map(msg => {return JSON.parse(msg.body);}));
  }

  public addPlayer(pname: string): Observable<ResponsePlayer>
  {
    return this.http.get<ResponsePlayer>(this.url + "/addPlayer", {params: {name: pname}});
  }

}
