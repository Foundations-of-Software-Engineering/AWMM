import { wsManager } from './websocketManager.js';

function sendMessage(data: {
    GAMEID: string
    USERID: number;
    weapon?: string;
    action?: string;
    location?: string;
    suspect?: string;
}): void {
    wsManager.sendMessage(data);
}

function startGame(data: {GAMEID: string, USERID: number, action: string }): void {
    wsManager.sendMessage(data);
}

export { sendMessage, startGame };
