import { wsManager } from './websocketManager.js';

function checkCookieExists() {
    const cookieValue = document.cookie.split("; ").find((row) => row.startsWith("gameId"))?.split("=")[1];
    if (cookieValue) return true;
    return false;
}

function redirectToPageIfCookieExists() {
    const checkInterval = 200
    const maxTries = 100
    let tries = 0

    const intervalId = setInterval(function () {
        if (checkCookieExists() || tries > maxTries) {
            clearInterval(intervalId);
            if (checkCookieExists()) {
                OpenNewTab("game.html");
            } else {
                console.error('Cookie was not set within the expected time.');
            }
        }
        tries++;
    }, checkInterval);
}

document.addEventListener('DOMContentLoaded', () => {
    wsManager.connect();

    // Set up an event listener for when messages are received
    wsManager.onMessage((event: MessageEvent) => {
        console.log('Message from server:', event.data);
        const message = JSON.parse(event.data)
        console.log("Message received:", JSON.stringify(message))
        if (message.type === 'GAMEID') {
            const gameId = message.GAMEID
            if (gameId != null){
                document.cookie = `gameId=${gameId}; path=/; max-age=86400`;
            }
            else {
                console.log("Could not join game.")
            }
        }
    });

    // Event listener for "Host a Game" button
    document.getElementById('hostGame')?.addEventListener('click', () => {
        const message = { action: 'hostGame' };
        console.log("TEST", message);
        wsManager.sendMessage( message );
        redirectToPageIfCookieExists();
    });

    // Event listener for "Quick Join" button
    document.getElementById('quickJoin')?.addEventListener('click', () => {
        wsManager.sendMessage({ action: 'quickJoin' });
        redirectToPageIfCookieExists();

    });

    // Event listener for "Join Private Game" button
    document.getElementById('joinPrivateGame')?.addEventListener('click', () => {
        const gameId = (document.getElementById('gameIdInput') as HTMLInputElement).value;
        if (gameId) {
            wsManager.sendMessage({ action: 'JOINGAME', GAMEID: gameId });
            redirectToPageIfCookieExists();
        } else {
            console.error('Game ID is required for joining a private game.');
        }
    });
});

function OpenNewTab(url: string) {
    window.open(url, "_blank");
}
