import { sendMessage } from "./sendMessage.js";
import { wsManager } from './websocketManager.js';


document.addEventListener("DOMContentLoaded", () => {
    wsManager.connect();


    const form = document.getElementById('messageForm') as HTMLFormElement;
    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const GAMEID = (document.getElementById('GAMEID') as HTMLInputElement).value;
        const USERID = (document.getElementById('USERID') as HTMLInputElement).value;
        const action = (document.getElementById('action') as HTMLInputElement).value;
        const location = (document.getElementById('location') as HTMLInputElement).value;
        const weapon = (document.getElementById('weapon') as HTMLInputElement).value;
        const suspect = (document.getElementById('suspect') as HTMLInputElement).value;

        try {
            const message = await sendMessage({ GAMEID, USERID, action, location, weapon, suspect });
            console.log('Message received: ', message);
            form.reset();
        } catch (error) {
            console.error(`Error sending message: `, error);
            alert(`Failed to send message.`);
        }
    });
});