import { InjectableRxStompConfig } from '@stomp/ng2-stompjs';
import { backendPort } from './../environments/environment';

export const RkStompConfig: InjectableRxStompConfig = {
    brokerURL: "ws://" + window.location.hostname + ":" + backendPort + "/ws",

    heartbeatIncoming: 0,
    heartbeatOutgoing: 20000,

    reconnectDelay: 200,

    debug: (msg: string): void => {
        console.log(new Date(), msg);
    }
}