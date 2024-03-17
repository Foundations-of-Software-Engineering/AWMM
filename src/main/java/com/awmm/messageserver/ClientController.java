package com.awmm.messageserver;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * Controller class for handling client requests.
 * @author AWMM
 */
@RestController
public class ClientController {

    /**
     * Handles POST requests to "/api/userInput".
     * Receives a message from the client, sends it to the Game server, and returns the response.
     *
     * @param requestBody The message sent by the client in JSON format.
     * @return The response message from the Game server in JSON format.
     */
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

    /**
     * Handles GET requests to "/api/userInput".
     * This method is not implemented, and it only logs a message indicating a GET request.
     */
    @GetMapping("/api/userInput")
    public void handleClientMessage(){
        System.out.println("A GET request was received at /api/userInput");
    }
}