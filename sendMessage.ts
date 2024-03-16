import axios from 'axios';

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById('messageForm') as HTMLFormElement;

    form.onsubmit = async (e) => {
        e.preventDefault();

        const userID = (document.getElementById('userID') as HTMLInputElement).value;
        const groupID = (document.getElementById('groupID') as HTMLInputElement).value;
        const message = (document.getElementById('message') as HTMLInputElement).value;

        const data = {
            userID,
            message,
            groupID
        };

        try {
            const response = await axios.post('<--servername-->', data);
            console.log('Message sent successfully: ', response.data);
            alert('Message sent successfully!');
        } catch (error) {
            console.error('Error sending message: ', error);
            alert('Failed to send message.');
        }
    }
});