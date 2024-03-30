import { wsManager } from './websocketManager.js';

function sendMessage(data: { GAMEID: string; USERID: string; action: string; location?: string; weapon?: string; suspect?: string; }): void {
    wsManager.sendMessage(data);
}

export { sendMessage };
