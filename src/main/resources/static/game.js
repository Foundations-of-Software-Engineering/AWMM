var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { sendMessage } from "./sendMessage.js";
import { wsManager } from './websocketManager.js';
const characterNames = {
    0: "Miss Scarlet",
    1: "Colonel Mustard",
    2: "Mrs White",
    3: "Mr Green",
    4: "Mrs Peacock",
    5: "Professor Plum"
};
function showCookies() {
    var _a;
    const cookieValue = (_a = document.cookie.split("; ").find((row) => row.startsWith("gameId"))) === null || _a === void 0 ? void 0 : _a.split("=")[1];
    const output = document.getElementById("cookie-value");
    console.log("TEST", cookieValue);
    if (output)
        output.textContent = `>${cookieValue}`;
}
document.addEventListener("DOMContentLoaded", () => {
    wsManager.connect();
    const form = document.getElementById('messageForm');
    const imageSelection = document.getElementById('image-selection');
    const mainContent = document.getElementById('main-content');
    const selectableImages = document.querySelectorAll('.selectable-image');
    let selectedImageValue = null; // Variable to store the selected image value
    const messageBox = document.getElementById("message-box");
    form.style.display = 'none'; // Initially hide the form
    // Add event listeners to each selectable image
    selectableImages.forEach(image => {
        image.addEventListener('click', () => {
            // Store the selected image value in the variable
            selectedImageValue = parseInt(image.getAttribute('data-image'));
            document.cookie = `userId=${selectedImageValue}; path=/; max-age=86400`;
            imageSelection.style.display = 'none'; // Hide the image selection
            mainContent.style.display = 'block'; // Show the main content
            form.style.display = 'block'; // Show the form
        });
    });
    // Set up an event listener for when messages are received
    wsManager.onMessage((event) => {
        console.log('Message from server:', event.data);
        const message = JSON.parse(event.data);
        if (message.action === 'SUCCESS') {
            const characterName = characterNames[message.USERID];
            console.log(`${characterName} has joined the game.`);
            messageBox.innerHTML += `${characterName} has joined the game.<br>`;
        }
    });
    form.addEventListener("submit", (event) => __awaiter(void 0, void 0, void 0, function* () {
        event.preventDefault();
        const GAMEID = document.getElementById('GAMEID').value;
        const USERID = Number(document.getElementById('USERID').value);
        const action = document.getElementById('action').value;
        const location = document.getElementById('location').value;
        const weapon = document.getElementById('weapon').value;
        const suspect = document.getElementById('suspect').value;
        const cookiebtn = document.getElementById("show-cookie-btn");
        if (cookiebtn) {
            cookiebtn.addEventListener("click", showCookies);
        }
        else {
            console.error("Button with id 'show-cookie-btn' not found.");
        }
        try {
            const message = yield sendMessage({ GAMEID, USERID, action, location, weapon, suspect });
            // sendMessage() is void and so message will be undefined
            // console.log('Message received: ', message);
            form.reset();
        }
        catch (error) {
            console.error(`Error sending message: `, error);
            alert(`Failed to send message.`);
        }
    }));
});
