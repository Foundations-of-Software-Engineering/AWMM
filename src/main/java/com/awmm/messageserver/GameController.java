package com.awmm.messageserver;

import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

@Controller
public class GameController {

    public static String run(String message) throws IOException{
        final int port = 5555;
        final InetAddress bindAddress = InetAddress.getByName("127.0.0.1");
        String response = "";


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