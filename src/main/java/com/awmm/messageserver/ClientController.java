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
import com.awmm.messageserver.board.Board;

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
    private final GameController gameController = new GameController();
    
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
                case "MOVE":
                	// TODO
                	gameController.handleMove(clientMessage);
                	break;
                case "SUGGEST":
                	// TODO 
                	gameController.handleSuggest(clientMessage);
                	break;
                case "ACCUSE":
                	// TODO
                	break;
                case "DISPROVE":
                	// TODO
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
     * Client must provide a valid userID between 0 and 5.
     * If gameID is not null or empty, it is a returning user.
     * Otherwise, it is a new user 
     * @param session The WebSocket session representing the client connection.
     * @param clientMessage The message received from the client.
     */
    public void handleLoginAction(WebSocketSession session, Message clientMessage) {
    	String gameID = clientMessage.GAMEID();
    	int userID = clientMessage.USERID();
    	Message responseMessage;
    	boolean success = false;
        if (userID < 0 || userID > 5) return;
    	if (gameID != null && !gameID.isBlank()) {
    		// when gameid is not null, it is an old user trying to regain access
    		if (!userIdSessionMap.containsKey(gameID + userID)) {
    			// if no session exists, we create a new session for old user
    			responseMessage = new Message(gameID, userID, "SUCCESS", null, null, null);
    			success = true;
    		}
    		else {
    			// if a session does exist for this user, fail 
    			responseMessage = new Message(gameID, userID, "FAIL", null, null, null);
    		}
        }
        else {
        	// new user
        	gameID = UUID.randomUUID().toString();
        	responseMessage = new Message(gameID, userID, "SUCCESS", null, null, null);
        	success = true;
        }
    	
        sendMessageToClient(session, responseMessage);

        // Save userID mapped to session
        if (success) {        	
        	userIdSessionMap.put(gameID + userID, session);
        	sessionToUserIdMap.put(session, gameID + userID);
        }
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
