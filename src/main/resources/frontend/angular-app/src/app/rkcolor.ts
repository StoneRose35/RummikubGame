
export class RKColor
{
    rgb: String;
    code: number;
    name: String;

    
    constructor(c: number)
    {
        if (c==1){
            this.name="black";
            this.rgb="#000000";
            this.code = c;
        }
        else if (c==2)
        {
            this.name="red";
            this.rgb="#ff0000";
            this.code = c;           
        }
        else if (c==3)
        {
            this.name="gold";
            this.rgb="#d8d800";
            this.code = c;  
        }

        else if (c==4)
        {
            this.name="blue";
            this.rgb="#0000ff";
            this.code = c;  
        }
    }
    
}