
import {RKColor} from './rkcolor'

export class Figure
{
    color: RKColor;
    instance: number;
    number: number;

    constructor(color: RKColor, instance: number, value: number)
    {
        this.color=color;
        this.instance=instance;
        this.number=value;
    }
}

