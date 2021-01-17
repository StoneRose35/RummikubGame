import { Component, Input, OnInit } from '@angular/core';
import {RKColor} from './../rkcolor'
import {Figure} from './../figure'

@Component({
  selector: 'app-figure',
  templateUrl: './figure.component.html',
  styleUrls: ['./figure.component.scss']
})
export class FigureComponent implements OnInit {

  @Input()
  figure: Figure;
  clr: String;
  repr: String;

  constructor() { }

  ngOnInit(): void {
    if (this.figure.instance <3 )
    {
      this.repr = this.figure.number + "";
    }
    else if (this.figure.instance < 5)
    {
      if (this.figure.number > 0)
      {
        this.repr = "J (" + this.figure.number + ")";
      }
      else
      {
        this.repr = "J";
      }
    }
    else
    {
      this.repr = "+";
    }
    this.clr = this.figure.color.rgb;
  }

}
