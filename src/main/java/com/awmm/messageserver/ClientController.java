package com.awmm.messageserver;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ClientController {

    @PostMapping("/api/userInput")
    public Message handleClientMessage(@RequestBody Message requestBody)
    {
        System.out.println("A POST request was received at /api/userInput");
        System.out.println("Sending to Game server.");
        String response = null;
        try {
            response = GameController.run(requestBody.suspect());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finished communication with Game server.");

        Message returnMessage = new Message(requestBody.gameID(), requestBody.userID(),
                requestBody.action(), requestBody.location(), requestBody.weapon(), response);

        System.out.println("Return message:\n" + returnMessage);
        return returnMessage;
    }

    @GetMapping("/api/userInput")
    public void handleClientMessage(){
        System.out.println("A GET request was received at /api/userInput");
    }
}