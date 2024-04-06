import { wsManager } from './websocketManager.js';

function sendMessage(data: { GAMEID: number; USERID: number; action: string; location?: string; weapon?: string; suspect?: string; }): void {
    wsManager.sendMessage(data);
}

export { sendMessage };
