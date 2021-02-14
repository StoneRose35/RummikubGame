import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Player } from './../game.service';


@Component({
  selector: 'app-player-overview',
  templateUrl: './player-overview.component.html',
  styleUrls: ['./player-overview.component.scss']
})
export class PlayerOverviewComponent implements OnInit {

  @Input()
  player: Player;

  currentSVG: boolean;
  SVGFile: SafeUrl;

  constructor(protected sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.currentSVG = true;
    this.SVGFile = this.sanitizer.bypassSecurityTrustHtml(this.player.avatar);
  }

}
