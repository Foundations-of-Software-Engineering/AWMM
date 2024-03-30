import { wsManager } from './websocketManager.js';

document.addEventListener('DOMContentLoaded', () => {
    wsManager.connect();

    // Set up an event listener for when messages are received
    wsManager.onMessage((event: MessageEvent) => {
        console.log('Message from server:', event.data);
        // Here, you can add logic to handle incoming messages
    });

    // Event listener for "Host a Game" button
    document.getElementById('hostGame')?.addEventListener('click', () => {
        const message = { action: 'hostGame' };
        console.log("TEST", message);
        wsManager.sendMessage( message );
    });

    // Event listener for "Quick Join" button
    document.getElementById('quickJoin')?.addEventListener('click', () => {
        wsManager.sendMessage({ action: 'quickJoin' });
    });

    // Event listener for "Join Private Game" button
    document.getElementById('joinPrivateGame')?.addEventListener('click', () => {
        const gameId = (document.getElementById('gameIdInput') as HTMLInputElement).value;
        if (gameId) {
            wsManager.sendMessage({ action: 'joinPrivateGame', gameId });
        } else {
            console.error('Game ID is required for joining a private game.');
        }
    });
});
