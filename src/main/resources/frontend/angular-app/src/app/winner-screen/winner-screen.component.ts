import { Component, OnInit } from '@angular/core';
import { Player } from '../game.service';

@Component({
  selector: 'app-winner-screen',
  templateUrl: './winner-screen.component.html',
  styleUrls: ['./winner-screen.component.scss']
})
export class WinnerScreenComponent implements OnInit {

  players: Array<Player>;
  constructor() { }

  ngOnInit(): void {
  }

}
