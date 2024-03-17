
async function sendMessage(data: { GAMEID: number; USERID: number; action: string; location?: string; weapon?: string; suspect?: string; }): Promise<void> {
	const response = await fetch('<---server name--->', {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(data),
	});

	if (!response.ok) {
		throw new Error('Failed to send message');
	}

	console.log("Message sent successfully:", await response.json());
}

export { sendMessage };