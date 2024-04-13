import {sendMessage} from "./sendMessage.js";
import {wsManager} from './websocketManager.js';

const characterNames: {[key: number]: string } = {
    0: "Miss Scarlet",
    1: "Colonel Mustard",
    2: "Mrs White",
    3: "Mr Green",
    4: "Mrs Peacock",
    5: "Professor Plum"
}

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
    const imageSelection = document.getElementById('image-selection')!;
    const mainContent = document.getElementById('main-content')!;
    const selectableImages = document.querySelectorAll('.selectable-image');
    let selectedImageValue: number | null = null; // Variable to store the selected image value

    form.style.display = 'none'; // Initially hide the form

    // Add event listeners to each selectable image
    selectableImages.forEach(image => {
        image.addEventListener('click', () => {
            // Store the selected image value in the variable
            selectedImageValue = parseInt(image.getAttribute('data-image')!);
            document.cookie = `userId=${selectedImageValue}; path=/; max-age=86400`;

            imageSelection.style.display = 'none'; // Hide the image selection
            mainContent.style.display = 'block'; // Show the main content
            form.style.display = 'block'; // Show the form
        });
    });

    // Set up an event listener for when messages are received
    wsManager.onMessage((event: MessageEvent) => {
        console.log('Message from server:', event.data);
        const message = JSON.parse(event.data)
        if (message.action === 'SUCCESS') {
            const characterName = characterNames[message.USERID];
            console.log(`${characterName} has joined the game.`)
        }
    });

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const GAMEID = (document.getElementById('GAMEID') as HTMLInputElement).value;
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
            // sendMessage() is void and so message will be undefined
            // console.log('Message received: ', message);
            form.reset();
        } catch (error) {
            console.error(`Error sending message: `, error);
            alert(`Failed to send message.`);
        }
    });
});