
import {RKColor} from './rkcolor'

export class Figure
{
    colorcode: number;
    instance: number;
    number: number;
    position: number;
    shelfNr: number;
    representation: string;
    clr: String;

    constructor(colorcode: number, instance: number, value: number, shelfNr: number, position: number)
    {
        this.colorcode=colorcode;
        this.instance=instance;
        this.number=value;
        this.shelfNr=shelfNr;
        this.position=position; 
        this.representation = this.repr();
        this.clr = new RKColor(this.colorcode).rgb;
    }

    colorstring(): String
    {
      return new RKColor(this.colorcode).rgb;
    }

    repr(): string
    {
        var representation;
        if (this.instance <3 )
        {
            representation = this.number + "";
        }
        else if (this.instance < 5)
        {
          if (this.number > 0)
          {
            representation = "J (" + this.number + ")";
          }
          else
          {
            representation = "J";
          }
        }
        else
        {
          representation = "+";
        }
        return representation;
    }

}

