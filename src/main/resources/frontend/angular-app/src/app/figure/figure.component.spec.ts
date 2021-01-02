import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FigureComponent } from './figure.component';
import { Figure } from './../figure'
import { Component } from '@angular/core';
import { RKColor } from '../rkcolor';

describe('FigureComponent', () => {
  let component: TestCmpWrapper;
  let fixture: ComponentFixture<TestCmpWrapper>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FigureComponent, TestCmpWrapper ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestCmpWrapper);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it("should show the number 12", () => {
    expect(fixture.nativeElement.querySelector("h3").innerText).toEqual("12");
  });
});



@Component({
  selector: "test-comp",
  template: '<app-figure [figure]="mockFigure"></app-figure>',

})
class TestCmpWrapper {
  mockFigure = new Figure(new RKColor(0),0,12);
}