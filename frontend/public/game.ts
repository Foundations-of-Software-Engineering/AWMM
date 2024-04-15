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

    const suggestAction = document.getElementById('suggestAction') as HTMLInputElement;
    const accuseAction = document.getElementById('accuseAction') as HTMLInputElement;

    const selectOptions = document.getElementById('selectOptions')!;


    const suspectSelect = document.getElementById('suspectSelect') as HTMLSelectElement;
    const weaponSelect = document.getElementById('weaponSelect') as HTMLSelectElement;
    const roomSelect = document.getElementById('roomSelect') as HTMLSelectElement;

    const suspects = ['Professor Plum', 'Miss Scarlet', 'Col. Mustard', 'Mrs. Peacock', 'Mr. Green', 'Mrs. White'];
    const weapons = ['Candlestick', 'Dagger', 'Lead Pipe', 'Revolver', 'Rope', 'Wrench'];
    const rooms = ['Study', 'Hall', 'Lounge', 'Kitchen', 'Ballroom', 'Conservatory', 'Dining Room', 'Billiard Room', 'Library'];


    suspects.forEach(suspects => {
        const option = document.createElement('option');
        option.value = suspects;
        option.textContent = suspects;
        suspectSelect.appendChild(option);
    });

    weapons.forEach(weapon => {
        const option = document.createElement('option');
        option.value = weapon;
        option.textContent = weapon;
        weaponSelect.appendChild(option);
    });

    rooms.forEach(rooms => {
        const option = document.createElement('option');
        option.value = rooms;
        option.textContent = rooms;
        roomSelect.appendChild(option);
    });


    document.querySelectorAll('input[name="actionChoice"]').forEach(input => {
        input.addEventListener('change', function () {
            if (suggestAction.checked || accuseAction.checked) {
                selectOptions.style.display = 'block';
            } else {
                selectOptions.style.display = 'none';
            }
        })
    });

    // Function to toggle select options based on the checked radio button
    function toggleSelectOptions() {
        if (suggestAction.checked) {
            selectOptions.style.display = 'block';
        } else {
            selectOptions.style.display = 'none';
        }
    }

    startButton.addEventListener("click", async () => {
        try {
            const action = startButton.value;
            const gameID = <string>getCookieValue('gameId');
            const userID = parseInt(<string>getCookieValue('userId'));
            const message = await startGame({ action: action, GAMEID: gameID, USERID: userID });
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
        var isEmpty = messageBox.innerHTML === "";
        if (isEmpty) {
            messageBox.innerHTML += 'New Game Created with Game ID: ' + message.GAMEID + '<br>';
        }
        if (message.type === 'LOGIN') {
            const characterName = characterNames[message.USERID];
            if (message.action === 'SUCCESS') {
                console.log(`Login succeeded for ${characterName}`)
                messageBox.innerHTML += `${characterName} has joined the game.<br>`;
            } else {
                console.log(`Login failed for ${characterName}`)
            }
            if (startButtonContainer) {
                startButtonContainer.style.display = 'block'; // Safe to access `style` because we checked if it's not null
            } else {
                console.error('Failed to find the startButtonContainer element.');
            }
        } else if (message.type === 'start') {
            console.log('Start game message received:', message);
            startButton.style.display = 'none';
            mainContent.style.display = 'block'; // Show the main content
            form.style.display = 'block'; // Show the form
        } else if (message.type === 'SUGGEST') {
            messageBox.innerHTML += `${characterNames[message.USERID]} suggests it was ${message.suspect} in the ${message.location} with a ${message.weapon}.<br>`;
        } else if (message.type === 'accusefail') {
            form.style.display = 'none';
        }
    });

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const gameId = <string>getCookieValue('gameId');
        const userId = parseInt(<string>getCookieValue('userId'));
        const actionSelected = document.querySelector<HTMLInputElement>('input[name="actionChoice"]:checked')!;

        const data = {
            GAMEID: gameId,
            USERID: userId,
            action: actionSelected.value,
            location: 'null',
            weapon: 'null',
            suspect: 'null'
        };


        if (actionSelected.value === "SUGGEST" || actionSelected.value === "ACCUSE") {
            data.location = (document.getElementById('roomSelect') as HTMLInputElement).value;
            data.weapon = (document.getElementById('weaponSelect') as HTMLInputElement).value;
            data.suspect = (document.getElementById('suspectSelect') as HTMLInputElement).value;
        } else { 
            if (actionSelected.id === 'leftAction') {
                data.location = 'left';
            } else if (actionSelected.id === 'rightAction') {
                data.location = 'right';
            } else if (actionSelected.id === 'upAction') {
                data.location = 'up';
            } else if (actionSelected.id === 'downAction') {
                data.location = 'down';
            } else if (actionSelected.id === 'diagonalAction') {
                data.location = 'diagonal';
            } else {
                console.error("Direction not available.");
            }
        }

        data.action = actionSelected.value;

        try {
            const message = await sendMessage(data);
            // sendMessage() is void and so message will be undefined
             console.log('Message received: ', message);
            form.reset();
        } catch (error) {
            console.error(`Error sending message: `, error);
            alert(`Failed to send message.`);
        }
    });
    // Initial call to set the correct display state based on the default checked radio
    toggleSelectOptions();
});