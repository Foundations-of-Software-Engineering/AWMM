import { sendMessage } from "./sendMessage.js";
import { wsManager } from './websocketManager.js';

function showCookies() {
    const cookieValue = document.cookie.split("; ").find((row) => row.startsWith("gameId"))?.split("=")[1];
    const output: HTMLElement | null = document.getElementById("cookie-value");
    console.log("TEST", cookieValue);
    if (output)
        output.textContent = `>${cookieValue}`;
}

document.addEventListener("DOMContentLoaded", () => {
    wsManager.connect();


    const form = document.getElementById('messageForm') as HTMLFormElement;
    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const GAMEID = Number((document.getElementById('GAMEID') as HTMLInputElement).value);
        const USERID = Number((document.getElementById('USERID') as HTMLInputElement).value);
        const action = (document.getElementById('action') as HTMLInputElement).value;
        const location = (document.getElementById('location') as HTMLInputElement).value;
        const weapon = (document.getElementById('weapon') as HTMLInputElement).value;
        const suspect = (document.getElementById('suspect') as HTMLInputElement).value;

        const cookiebtn: HTMLElement | null = document.getElementById("show-cookie-btn");
        if (cookiebtn) {
            cookiebtn.addEventListener("click", showCookies)
        } else {
            console.error("Button with id 'show-cookie-btn' not found.");
        }


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