class WebSocketManager {
    private url: string;
    private socket: WebSocket | null = null;

    constructor(url: string) {
        this.url = url;
    }

    connect(): void {
        this.socket = new WebSocket(this.url);

        this.socket.addEventListener('open', () => {
            console.log('WebSocket connection established');
        });

        this.socket.addEventListener('close', () => {
            console.log('WebSocket connection closed');
        });

        this.socket.addEventListener('error', (error: Event) => {
            console.error('WebSocket error:', error);
        });
    }

    sendMessage(message: Record<string, unknown>): void {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            console.log(message);
            this.socket.send(JSON.stringify(message));
        } else {
            console.error('WebSocket is not open.');
        }
    }

    onMessage(callback: (event: MessageEvent) => void): void {
        this.socket?.addEventListener('message', callback);
    }
}

// Place game server below
export const wsManager = new WebSocketManager('ws://SERVERIP:8888/api/userInput');
