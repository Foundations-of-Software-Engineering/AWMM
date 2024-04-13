package com.awmm.messageserver;

import com.awmm.messageserver.messages.ExampleMessage;
import com.awmm.messageserver.messages.GameIdMessage;
import com.awmm.messageserver.messages.Message;
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
   
    private final Map<String, Sessions> gameID2UserID2Session = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, String> session2GameID = new ConcurrentHashMap<>();
    private final GameController gameController = new GameController();
    
    public GameController getGameController() {
		return gameController;
	}

	/**
	 * Inner class representing sessions for WebSocket connections.
	 * Maintains an array of WebSocketSession objects for each user position in the game.
	 * Provides methods to manipulate the sessions array, such as adding, removing, and retrieving sessions.
	 */
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
			ExampleMessage clientMessage = mapper.readValue(jsonText, ExampleMessage.class);
            switch (clientMessage.action().toUpperCase()) {
            	case "START":
            		handleStartAction(session, clientMessage);
            		break;
                case "LOGIN": // this adds player to board
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
                	// Send Message to a Player to Disprove if it's his/her turn
                	// Maybe automatically check for who can disprove and ask that player in order
                	
                	break;
				case "HOSTGAME":
					handleHostAction(session);
					break;
				case "JOINGAME":
					handleJoinAction(session, clientMessage);
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
	 * Handles the "START" action received from a WebSocket client.
	 * This method initiates the game start procedure by calling the corresponding method in the game controller.
	 * After handling the start action, it broadcasts a message to all clients in the same game.
	 *
	 * @param clientMessage The message received from the WebSocket client.
	 */
	private void handleStartAction(WebSocketSession session, ExampleMessage clientMessage) {
		logger.info("Start message received");
		gameController.handleStart(clientMessage);
		broadcastMessage(clientMessage, clientMessage.GAMEID());
	}


	/**
	 * Handles the "SUGGEST" action received from a WebSocket client.
	 * This method processes the suggestion made by a player in the game.
	 * After handling the suggestion action, it broadcasts a message to all clients in the same game.
	 *
	 * @param session The WebSocket session of the client.
	 * @param clientMessage The message received from the WebSocket client.
	 */
	private void handleSuggest(WebSocketSession session, ExampleMessage clientMessage) {
		gameController.handleSuggest(clientMessage);
		broadcastMessage(clientMessage, clientMessage.GAMEID());
		
	}

	/**
	 * Handles the "MOVE" action received from a WebSocket client.
	 * This method processes the move made by a player in the game.
	 * After handling the move action, it broadcasts a message to all clients in the same game.
	 *
	 * @param session The WebSocket session of the client.
	 * @param clientMessage The message received from the WebSocket client.
	 */
	private void handleMoveAction(WebSocketSession session, ExampleMessage clientMessage) {
		/*boolean success = */gameController.handleMove(clientMessage);
		// if (success) { tell everyone} else {tell player move failed and to make another move}
		broadcastMessage(clientMessage, clientMessage.GAMEID());
	}

//	private void sendGameId(WebSocketSession session) {
//		Message gameIdMessage = new GameIdMessage(session.getId(), "GAMEID");
//		sendMessageToClient(session, gameIdMessage);
//	}

	/**
	 * Handles the "HOSTGAME" action received from a WebSocket client.
	 * This method creates a new game board state and assigns a unique game ID to it.
	 * After handling the host game action, it sends a response message to the client indicating the success or failure
	 * of the operation.
	 *
	 * @param session The WebSocket session of the client.
	 */
	private void handleHostAction(WebSocketSession session) {
		String gameID = UUID.randomUUID().toString().substring(0,8);
		boolean success = gameController.createBoardState(gameID); // extremely low but non-zero chance of collision
		Message response;

		if (success) {
			response = new GameIdMessage(gameID, "GAMEID");
			logger.info("{} successfully created", gameID);
		} else {
			response = new GameIdMessage(null, "GAMEID");
		}
		sendMessageToClient(session, response);
	}

	/**
	 * Handles the "JOINGAME" action received from a WebSocket client.
	 * This method checks if a game is joinable and sends a response message to the client indicating the success or
	 * failure of the operation.
	 *
	 * @param session The WebSocket session of the client.
	 * @param clientMessage The message received from the WebSocket client.
	 */
	private void handleJoinAction(WebSocketSession session, ExampleMessage clientMessage) {
		String gameID = clientMessage.GAMEID();
		boolean success = gameController.isJoinable(gameID);
		Message response;

		// Iterate over sessions only if the game has yet to start
		if (success) {
			success = false;
			WebSocketSession[] sessions = gameID2UserID2Session.get(gameID).getSessions();
			for (WebSocketSession s: sessions){
				if (s == null){
					success = true;
					break;
				}
			}
		}

		if (success) {
			response = new GameIdMessage(gameID, "GAMEID");
			logger.info("Player joined game {}", gameID);
		} else {
			response = new GameIdMessage(null, "GAMEID");
			logger.info("Player tried to join full game {}", gameID);
		}
		sendMessageToClient(session, response);
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

	/**
	 * Broadcasts a message to all WebSocket clients in a specific game.
	 *
	 * @param message The message to broadcast.
	 * @param gameID The ID of the game to broadcast the message to.
	 */
    public void broadcastMessage(Message message, String gameID) {
    	String jsonMessage = convertToJson(message);
		logger.info("Message for broadcast: {}", jsonMessage);
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
    private void handleLoginAction(WebSocketSession session, ExampleMessage clientMessage) {
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
        	else if (gameID != null && !gameID.isBlank()) {
        		// join new game as new player
        		gameID2UserID2Session.put(gameID, new Sessions());
        		success = true;
        	}
        }

		ExampleMessage successResponseMessage = new ExampleMessage(gameID, userID, "SUCCESS", null, null, null, "example");
		ExampleMessage failureResponseMessage = new ExampleMessage(gameID, userID, "FAIL", null, null, null, "example");

        if (success) {
        	gameID2UserID2Session.get(gameID).put(userID, session);
        	session2GameID.put(session, gameID);        
        	gameController.addPlayer(gameID, userID);
        	broadcastMessage(successResponseMessage, gameID); // tell all users about new user
			logger.info("Login for GAMEID {} USERID {}", gameID, userID);
    	}
        else {
        	sendMessageToClient(session, failureResponseMessage);
			logger.error("Failed login for GAMEID {} USERID {}", gameID, userID);
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

	/**
	 * Retrieves the current state of the game for the specified game ID.
	 *
	 * @param gameID The ID of the game for which to retrieve the state.
	 * @return The current state of the game.
	 */
    public String getGameState(String gameID) {
    	return gameController.getBoardState(gameID);
    }
}
