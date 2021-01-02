import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-new-player-dialog',
  templateUrl: './new-player-dialog.component.html',
  styleUrls: ['./new-player-dialog.component.scss']
})
export class NewPlayerDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<NewPlayerDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string)
    {

    }


  ngOnInit(): void {
  }

}
