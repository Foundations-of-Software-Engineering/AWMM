import { sendMessage } from "./sendMessage.js";

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById('messageForm') as HTMLFormElement;

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const GAMEID = Number((document.getElementById('GAMEID') as HTMLInputElement).value);
        const USERID = Number((document.getElementById('USERID') as HTMLInputElement).value);
        const action = (document.getElementById('action') as HTMLInputElement).value;
        const location = (document.getElementById('location') as HTMLInputElement).value;
        const weapon = (document.getElementById('weapon') as HTMLInputElement).value;
        const suspect = (document.getElementById('suspect') as HTMLInputElement).value;

        try {
            await sendMessage({ GAMEID, USERID, action })
            console.log('Message received from ${data.USERID}: ${data.action}')
            alert('Message sent successfully!');
            form.reset()
        } catch (error) {
            console.error('Error sending message: ', error);
            alert('Failed to send message.');
        }
    });
});