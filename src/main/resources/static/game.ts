import { sendMessage, startGame } from "./sendMessage.js";
import { wsManager } from './websocketManager.js';

const characterNames: {[key: number]: string } = {
    0: "Miss Scarlet",
    1: "Colonel Mustard",
    2: "Mrs White",
    3: "Mr Green",
    4: "Mrs Peacock",
    5: "Professor Plum"
}

// Function to get the value of a specific cookie
function getCookieValue(cookieName: string): string | undefined {
    const cookie = document.cookie.split("; ").find((row) => row.startsWith(cookieName));
    return cookie ? cookie.split("=")[1] : undefined;
}

function sendLoginMessage(){
    const GAMEID = <string>getCookieValue('gameId');
    const USERID = parseInt(<string>getCookieValue('userId'));
    try {
        wsManager.sendMessage({GAMEID: GAMEID, USERID: USERID, action: "LOGIN"})
    } catch (error) {
        console.error("Error sending login message:", error);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    wsManager.connect();

    const form = document.getElementById('messageForm') as HTMLFormElement;
    const imageSelection = document.getElementById('image-selection')!;
    const mainContent = document.getElementById('main-content')!;
    const selectableImages = document.querySelectorAll('.selectable-image')!;
    let selectedImageValue: number | null = null; // Variable to store the selected image value
    const messageBox = document.getElementById("message-box")!;
    const startButton = document.getElementById('startButton') as HTMLInputElement;
    const startButtonContainer = document.getElementById('startButtonContainer');

    startButton.addEventListener("click", async () => {
        try {
            mainContent.style.display = 'block'; // Show the main content
            form.style.display = 'block'; // Show the form
            const action = startButton.value;
            const message = await startGame({ action });
            console.log('Start game message received:', message);
            startButton.style.display = 'none';
        } catch (error) {
            console.error(`Error starting game: `, error);
            alert(`Failed to start game.`);
        }
    });

    if (startButtonContainer) {
        startButtonContainer.style.display = 'block'; // Safe to access `style` because we checked if it's not null
    } else {
        console.error('Failed to find the startButtonContainer element.');
    }    form.style.display = 'none'; // Initially hide the form

    // Add event listeners to each selectable image
    selectableImages.forEach(image => {
        image.addEventListener('click', () => {
            // Store the selected image value in the variable
            selectedImageValue = parseInt(image.getAttribute('data-image')!);
            document.cookie = `userId=${selectedImageValue}; path=/; max-age=86400`;

            // Send login message with corresponding userid
            sendLoginMessage();

            imageSelection.style.display = 'none'; // Hide the image selection

        });
    });

    // Set up an event listener for when messages are received
    wsManager.onMessage((event: MessageEvent) => {
        console.log('Message from server:', event.data);
        const message = JSON.parse(event.data)
        if (message.action === 'SUCCESS') {
            const characterName = characterNames[message.USERID];
            console.log(`${characterName} has joined the game.`)
            messageBox.innerHTML += `${characterName} has joined the game.<br>`;
            if (startButtonContainer) {
                startButtonContainer.style.display = 'block'; // Safe to access `style` because we checked if it's not null
            } else {
                console.error('Failed to find the startButtonContainer element.');
            }
            if (message.action === 'accusefail') {
                form.style.display = 'none';
            }
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