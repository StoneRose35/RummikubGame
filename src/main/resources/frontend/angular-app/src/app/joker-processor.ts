import { Injectable } from '@angular/core';
import { Figure } from './figure';
import { RKColor } from './rkcolor';


@Injectable({
    providedIn: 'root'
  })
export class JokerProcessor {

    public process(line: Array<Figure>)
    {
        
        if (line.filter(f => f.instance > 2).length>0)
        {
            let lineWithoutJokers = line.filter(f => f.instance<3);
            let jokers = line.filter(f => f.instance > 2);
            if(lineWithoutJokers.filter((val,idx,fline) => fline.findIndex(v => v.colorcode === val.colorcode)===idx).length===1)
            {
                // same color, must be a series
                this.completeSeries(line);
            }
            else if(lineWithoutJokers.filter((val,idx,fline) => fline.findIndex(v => v.number === val.number)===idx).length===1)
            {
                // same value, must be a collection
                this.completeCollections(line);
            }
            else
            {
                // reset joker value, list is invalid anyways
                jokers.forEach(f => f.number=0);
            }
        }
    }

    public reset(line: Array<Figure>)
    {
        let jokers = line.filter(f => f.instance > 2);
        jokers.forEach(f => {
            f.number=0;
            f.colorcode=null;
        });

    }
    
    private completeSeries(line: Array<Figure>)
    {
        //check if missing number is in the middle
        let lineWithoutJokers = line.filter(f => f.instance<3);
        let lowestnr = lineWithoutJokers.sort((a,b) => a.number-b.number)[0].number;
        let highestnr = lineWithoutJokers.sort((a,b) => b.number-a.number)[0].number;
        let diff = (line.length)*(highestnr+lowestnr)/2.0 - lineWithoutJokers.map(f => f.number).reduce((a,b ) => a+b);
        if (diff > 0 && Number.isInteger(diff) && lineWithoutJokers.filter(f => f.number===diff).length===0)
        {
            // number missing 
            let jokers = line.filter(f => f.instance > 2);
            if (jokers.length > 1)
            {
                let jcntr = 0;
                let sorted = lineWithoutJokers.sort(f => f.number);
                for(let c=0;c<sorted.length;c++)
                {
                    if ((sorted[c+1].number-sorted[c].number) > 1)
                    {
                        jokers[jcntr].number = sorted[c].number + 1;
                        jokers[jcntr].colorcode = lineWithoutJokers[0].colorcode;
                        jcntr++;
                    }
                }
                if (jcntr < jokers.length)
                {
                    for(let jc=jcntr;jc<jokers.length;jc++)
                    {
                        jokers[jc].number = sorted[sorted.length-1].number + jc + 1;
                        jokers[jc].colorcode = lineWithoutJokers[0].colorcode;
                    }
                }
            }
            else
            {
                jokers[0].number = diff;
                jokers[0].colorcode=lineWithoutJokers[0].colorcode;
            }
        }
        else {
            // line is complete, set all jokers to the right side until 13 is reached then add to the left
            let sorted = lineWithoutJokers.sort((fa, fb) => fa.number-fb.number);
            let jokers = line.filter(f => f.instance > 2);
            let jc_left = 0;
            for(let jc=0;jc<jokers.length;jc++)
            {   
                if (sorted[sorted.length-1].number + jc + 1 < 14)
                {
                    jokers[jc].number = sorted[sorted.length-1].number + jc + 1;
                    jokers[jc].colorcode = lineWithoutJokers[0].colorcode;
                }
                else
                { // add left
                    jokers[jc].number = sorted[0].number - jc_left - 1;
                    jokers[jc].colorcode = lineWithoutJokers[0].colorcode;
                    jc_left -= 1;
                }
            }
        }

    }

    private completeCollections(line: Array<Figure>)
    {
        let allcolors = [1, 2, 3, 4];
        let lineWithoutJokers = line.filter(f => f.instance<3);
        let missingColors = allcolors.filter(ac => lineWithoutJokers.filter(el => el.colorcode=== ac).length === 0 );
        let jokers = line.filter(f => f.instance > 2);
        let mcCntr = 0;
        jokers.forEach(el => {
            el.colorcode = missingColors[mcCntr];
            el.number = lineWithoutJokers[0].number;
            mcCntr++;
        });
    }

}