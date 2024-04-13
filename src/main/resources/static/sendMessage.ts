import { wsManager } from './websocketManager.js';

function sendMessage(data: {
    weapon: string;
    USERID: number;
    action: string;
    location: string;
    suspect: string;
    GAMEID: string
}): void {
    wsManager.sendMessage(data);
}

function startGame(data: { action: string }): void {
    wsManager.sendMessage(data);
}

export { sendMessage, startGame };
