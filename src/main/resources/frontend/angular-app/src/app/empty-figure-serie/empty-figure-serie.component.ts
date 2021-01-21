import { Component, Input, OnInit, EventEmitter, Output } from '@angular/core';
import { Figure } from './../figure';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-empty-figure-serie',
  templateUrl: './empty-figure-serie.component.html',
  styleUrls: ['./empty-figure-serie.component.scss']
})
export class EmptyFigureSerieComponent implements OnInit {

  @Input()
  figures: Array<Figure>;

  @Input()
  disabled: Boolean;

  @Output() figuredropped: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  drop(event: CdkDragDrop<Figure[]>) {
      this.figuredropped.emit(event);
    }


  isEnabled(): boolean
  {
    return !this.disabled;
  }
}

