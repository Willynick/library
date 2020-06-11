import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'

let stompClient = null;
const handlers = []

export function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {}
    stompClient.connect({}, frame => {
        //console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/activity', book => {
            handlers.forEach(handler => handler(JSON.parse(book.body)));
        });
    });
}

export function addHandler(handler) {
    handlers.push(handler)
}

export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

export function sendMessage(book) {
    stompClient.send("/app/changeBook", {}, JSON.stringify(book));
}