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
//    private final Map<String, WebSocketSession> userIdSessionMap = new ConcurrentHashMap<>();
//    private final Map<WebSocketSession, String> sessionToUserIdMap = new ConcurrentHashMap<>();
   
    private final Map<String, Sessions> gameID2UserID2Session = new ConcurrentHashMap<String, Sessions>();
    private final Map<WebSocketSession, String> session2GameID = new ConcurrentHashMap<WebSocketSession, String>();
    private final GameController gameController = new GameController();
    
    private class Sessions {
    	
    	private WebSocketSession[] sessions;
    	
    	public Sessions() {
    		this.sessions = new WebSocketSession[6];
    	}
    	
    	public WebSocketSession[] getSessions() {
    		return this.sessions;
    	}
    	
    	public void remove(WebSocketSession session) {
    		for (WebSocketSession it : sessions) {
    			if (it.equals(session)) {
    				it = null;
    				return;
    			}
    		}
    	}
    	
    	public void put(int pos, WebSocketSession session) {
    		if (pos >= 0 && pos < 6) {
    			sessions[pos] = session;
    		}
    	}
    	
    	public WebSocketSession get(int pos) {
    		if (pos >= 0 && pos < 6) {
    			return sessions[pos];
    		}
    		else {
    			return null;
    		}
    	}
    }
    
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
            	case "START":
            		handleStartAction(session, clientMessage);
            		break;
                case "LOGIN":
                    handleLoginAction(session, clientMessage);
                    break;
                case "MOVE":
                	handleMoveAction(session, clientMessage);
                	break;
                case "SUGGEST":
                	// TODO 
                	handleSuggest(session, clientMessage);
                	break;
                case "ACCUSE":
                	// TODO
                	break;
                case "DISPROVE":
                	// TODO
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

	private void handleStartAction(WebSocketSession session, Message clientMessage) {
		gameController.handleStart(clientMessage);
		broadcastMessage(clientMessage, clientMessage.GAMEID());
	}

	private void handleHostAction(WebSocketSession session, Message clientMessage) {
		Message response = new Message ("10", 6, "host", null, null, null);
		sendMessageToClient(session, response);
	}

	private void handleSuggest(WebSocketSession session, Message clientMessage) {
		gameController.handleSuggest(clientMessage);
		broadcastMessage(clientMessage, clientMessage.GAMEID());
		
	}

	private void handleMoveAction(WebSocketSession session, Message clientMessage) {
		gameController.handleMove(clientMessage);
		broadcastMessage(clientMessage, clientMessage.GAMEID());
	}

	/**
     * Called after a WebSocket connection has been closed.
     * @param session The WebSocket session that has been closed.
     * @param closeStatus The status code indicating the reason for closure.
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, CloseStatus closeStatus) {
        logger.info("Connection closed with session: {} status {}", session.getId(), closeStatus.getCode());

        String gameID = session2GameID.remove(session);
        if (gameID != null) {
        	gameID2UserID2Session.get(gameID).remove(session);
        }
    }

    public void broadcastMessage(Message message, String gameID) {
    	String jsonMessage = convertToJson(message);
    	for (WebSocketSession session : gameID2UserID2Session.get(gameID).getSessions()) {
            try {
            	if (session != null) {            		
            		session.sendMessage(new TextMessage(jsonMessage));
            	}
            } catch (IOException e) {
                logger.error("Error broadcasting message to session: {}", session.getId(), e);
            }
    	}
    }
    
//    /**
//     * Broadcasts a message to all connected WebSocket clients.
//     * @param message The message to broadcast.
//     */
//    public void broadcastMessage(Message message) {
//        String jsonMessage = convertToJson(message);
//        for (WebSocketSession session: userIdSessionMap.values()) {
//            try {
//                session.sendMessage(new TextMessage(jsonMessage));
//            } catch (IOException e) {
//                logger.error("Error broadcasting message to session: {}", session.getId(), e);
//            }
//        }
//    }

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
     * Handles the LOGIN action from a WebSocket client for new and returning users.
     * If gameID is set, the user is trying to join an existing game.
     * If gameID is not set, a new game is created.
     * @param session The WebSocket session representing the client connection.
     * @param clientMessage The message received from the client.
     */
    private void handleLoginAction(WebSocketSession session, Message clientMessage) {
    	String gameID = clientMessage.GAMEID();
    	int userID = clientMessage.USERID();
   	
    	boolean success = false;
    	
        if (userID >= 0 && userID < 6) {        	
        	if (gameID != null && 
    			!gameID.isBlank() &&
    			gameID2UserID2Session.containsKey(gameID) && 
    			gameID2UserID2Session.get(gameID).get(userID) == null) { 
    			// join existing game
        		success = true;
        	}
        	else {
        		// create and join new game
        		gameID = UUID.randomUUID().toString();
        		gameID2UserID2Session.put(gameID, new Sessions());
        		gameController.createBoardState(gameID);
        		success = true;
        	}
        }
    	
     	Message successResponseMessage = new Message(gameID, userID, "SUCCESS", null, null, null);
    	Message failureResponseMessage = new Message(gameID, userID, "FAIL", null, null, null);
            
        if (success) {
        	gameID2UserID2Session.get(gameID).put(userID, session);
        	session2GameID.put(session, gameID);        
        	broadcastMessage(successResponseMessage, gameID); // tell all users about new user
    	}
        else {
        	sendMessageToClient(session, failureResponseMessage);
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
    
    public String getGameState(String gameID) {
    	return gameController.getBoardState(gameID);
    }
}
