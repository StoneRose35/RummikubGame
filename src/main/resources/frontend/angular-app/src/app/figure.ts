
import {RKColor} from './rkcolor'

export class Figure
{
    colorcode: number;
    instance: number;
    number: number;

    constructor(colorcode: number, instance: number, value: number)
    {
        this.colorcode=colorcode;
        this.instance=instance;
        this.number=value;
    }
}

