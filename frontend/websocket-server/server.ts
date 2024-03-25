import { WebSocketServer } from 'ws';

const wss = new WebSocketServer({ port: 8081 });

wss.on('connection', function connection(ws: WebSocketServer) {
    console.log('A client connected');

    ws.on('message', function incoming(data: string) {
        const message = data.toString();
        console.log('received:', message);
        try {
            const jsonData = JSON.parse(message);
            console.log(jsonData);
        } catch (e) {
            console.log('Error parsing JSON: ', e);
        }

    });

    ws.on('close', () => console.log('Client disconnected'));
});

console.log('WebSocket server started on ws://localhost:8081');
