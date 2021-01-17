import { Component, Input, OnInit, EventEmitter, Output } from '@angular/core';
import { Figure } from './../figure';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import { JokerProcessor } from './../joker-processor';

@Component({
  selector: 'app-figure-serie',
  templateUrl: './figure-serie.component.html',
  styleUrls: ['./figure-serie.component.scss']
})
export class FigureSerieComponent implements OnInit {

  @Input()
  figures: Array<Figure>;

  @Input()
  disabled: Boolean;

  @Output() figuredropped: EventEmitter<any> = new EventEmitter();

  constructor(
    public jp: JokerProcessor
  ) { 
    this.figures=[];
  }

  ngOnInit(): void {
  }

  drop(event: CdkDragDrop<Figure[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
                        event.container.data,
                        event.previousIndex,
                        event.currentIndex);
      this.jp.process(event.container.data);
      this.figuredropped.emit();
    }
  }

}
