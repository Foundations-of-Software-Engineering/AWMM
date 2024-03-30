package com.awmm.messageserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for handling WebSocket connections and messages from clients.
 * @author AWMM
 */
@Component
public class ClientController extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final Map<String, WebSocketSession> userIdSessionMap = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, String> sessionToUserIdMap = new ConcurrentHashMap<>();

    /**
     * Handles incoming text messages from WebSocket clients.
     * This method is called when a client sends a text message over WebSocket.
     * @param session The WebSocket session.
     * @param message The text message received from the client.
     */
    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {
        try {
            String jsonText = message.getPayload();
            Message clientMessage = mapper.readValue(jsonText, Message.class);
            switch (clientMessage.action().toUpperCase()) {
                case "LOGIN":
                    handleLoginAction(session, clientMessage);
                    break;
                case "HOSTGAME":
                    handleHostAction(session, clientMessage);
                    break;
                // Add other actions
                default:
                    logger.error("Unknown action received: {}", clientMessage.action());
            }
        } catch (JsonProcessingException e) {
            logger.error("Error processing incoming message from session: {}",session.getId(), e);
        }
    }

    /**
     * Called after a WebSocket connection has been closed.
     * @param session The WebSocket session that has been closed.
     * @param closeStatus The status code indicating the reason for closure.
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, CloseStatus closeStatus) {
        logger.info("Connection closed with session: {} status {}", session.getId(), closeStatus.getCode());

        // 1 Map in each direction to avoid looping through the map
        String userId = sessionToUserIdMap.remove(session);
        if (userId != null) {
            userIdSessionMap.remove(userId);
        }
    }

    /**
     * Broadcasts a message to all connected WebSocket clients.
     * @param message The message to broadcast.
     */
    public void broadcastMessage(Message message) {
        String jsonMessage = convertToJson(message);
        for (WebSocketSession session: userIdSessionMap.values()) {
            try {
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                logger.error("Error broadcasting message to session: {}", session.getId(), e);
            }
        }
    }

    /**
     * Sends a message to a specific WebSocket client.
     * @param session The WebSocket session of the client.
     * @param message The message to send.
     */
    public void sendMessageToClient(WebSocketSession session, Message message) {
        String jsonMessage = convertToJson(message);
        try {
            session.sendMessage(new TextMessage(jsonMessage));
        } catch (IOException e) {
            logger.error("Error sending message to session: {}",session.getId(), e);
        }
    }

    /**
     * Handles the LOGIN action from a WebSocket client.
     * If the client does not provide a USERID, generates a new one and assigns it to the client.
     * @param session The WebSocket session representing the client connection.
     * @param clientMessage The message received from the client.
     */
    public void handleLoginAction(WebSocketSession session, Message clientMessage) {
        String userID = clientMessage.USERID();

        // If user ID is default value, assign new user ID
        if (clientMessage.USERID().equals("0")){
            UUID uuid = UUID.randomUUID();
            userID = uuid.toString();

            // Create login message assigning userID to client, all other fields null
            Message responseMessage = new Message(null, userID,"LOGIN",null,null,null);
            sendMessageToClient(session, responseMessage);
        } else {
            Message responseMessage = new Message(null, userID,"LOGIN",null,null,null);
            sendMessageToClient(session, responseMessage);
        }

        // Save userID mapped to session
        userIdSessionMap.put(userID, session);
        sessionToUserIdMap.put(session, userID);
    }

    public void handleHostAction(WebSocketSession session, Message clientMessage) {
        UUID uuid = UUID.randomUUID();
        String gameID = uuid.toString().substring(0, 8);

        // Logic for starting the game with the game backend with new gameid

        // Send gameid to client
        Message responseMessage = new Message(gameID, null, "HOSTGAME", null, null, null);
        sendMessageToClient(session, responseMessage);
    }

    /**
     * Converts a message object to its JSON representation.
     * @param message The message to convert.
     * @return The JSON representation of the message.
     */
    public String convertToJson(Message message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            logger.error("Error converting message to JSON", e);
            return null;
        }
    }
}
