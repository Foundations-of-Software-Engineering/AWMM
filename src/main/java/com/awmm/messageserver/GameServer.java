package com.awmm.messageserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static void main(String[] args) throws IOException{
        final int port = 5555;
        final int backlog = 4;
        final InetAddress bindAddress = InetAddress.getByName("127.0.0.1");

        try (ServerSocket serverSocket = new ServerSocket(port, backlog, bindAddress)){
            System.out.println("Server is listening on port: " + port);

            while (true){
                Socket socket = serverSocket.accept();

                System.out.println("Client connected");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuilder response = new StringBuilder();
                response.append(reader.readLine());

                response.reverse();

                System.out.println("Message returned: " + response);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println(response);
                socket.close();
            }
        }
    }
}