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
console.log("TEST");
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById('messageForm');
    form.addEventListener("submit", (event) => __awaiter(void 0, void 0, void 0, function* () {
        event.preventDefault();
        const GAMEID = Number(document.getElementById('GAMEID').value);
        const USERID = Number(document.getElementById('USERID').value);
        const action = document.getElementById('action').value;
        const location = document.getElementById('location').value;
        const weapon = document.getElementById('weapon').value;
        const suspect = document.getElementById('suspect').value;
        try {
            yield sendMessage({ GAMEID, USERID, action });
            console.log(`Message received from ${USERID}: ${action}`);
            alert(`Message sent successfully!`);
            form.reset();
        }
        catch (error) {
            console.error(`Error sending message: `, error);
            alert(`Failed to send message.`);
        }
    }));
});
