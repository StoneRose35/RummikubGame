import { JokerProcessor} from './app/joker-processor';
import { RKColor } from './app/rkcolor';

let jp: JokerProcessor;
jp = new JokerProcessor();

let testdata=[{color: new RKColor(1),instance: 1, number: 12},
    {color: new RKColor(3),instance: 1, number: 12},
    {color: new RKColor(1),instance: 4, number: 0},
];
jp.process(testdata);
let msg="Hallo Welt";
console.log(msg);
