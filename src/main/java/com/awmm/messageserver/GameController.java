package com.awmm.messageserver;

import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Controller class for communicating with the game server.
 * @author AWMM
 */
@Controller
public class GameController {

    /**
     * Sends a message to the game server and receives a response.
     *
     * @param message The message to be sent to the game server.
     * @return The response from the game server.
     * @throws IOException If an I/O error occurs while communicating with the game server.
     */
    public static String run(String message) throws IOException{
        final int port = 5555;
        final InetAddress bindAddress = InetAddress.getByName("127.0.0.1");
        String response;

        try (Socket socket = new Socket(bindAddress, port)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            response = reader.readLine();

            System.out.println("Reply from Game server: " + response);
        }

        return response;

    }
}