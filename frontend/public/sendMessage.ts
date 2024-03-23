const socket = new WebSocket('ws://localhost:8080/');

async function sendMessage(data: { GAMEID: number; USERID: number; action: string; location?: string; weapon?: string; suspect?: string; }): Promise<void> {
	return new Promise((resolve, reject) => {
		socket.addEventListener('open', () => {
			socket.send(JSON.stringify(data));
			resolve();
		});
		socket.addEventListener('error', (error) => {
			reject(error);
		});
	});
}

export { sendMessage };