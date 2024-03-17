"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const axios_1 = __importDefault(require("axios"));
document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById('messageForm');
    form.onsubmit = (e) => __awaiter(this, void 0, void 0, function* () {
        e.preventDefault();
        const userID = document.getElementById('userID').value;
        const groupID = document.getElementById('groupID').value;
        const message = document.getElementById('message').value;
        const data = {
            userID,
            message,
            groupID
        };
        try {
            const response = yield axios_1.default.post('<--servername-->', data);
            console.log('Message sent successfully: ', response.data);
            alert('Message sent successfully!');
        }
        catch (error) {
            console.error('Error sending message: ', error);
            alert('Failed to send message.');
        }
    });
});
