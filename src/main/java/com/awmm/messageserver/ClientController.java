package com.awmm.messageserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.awmm.messageserver.chat.ChatController;
import com.awmm.messageserver.messages.AccuseFailMessage;
import com.awmm.messageserver.messages.ConfirmStartMessage;
import com.awmm.messageserver.messages.ExampleMessage;
import com.awmm.messageserver.messages.GameIdMessage;
import com.awmm.messageserver.messages.Message;
import com.awmm.messageserver.messages.NoWinMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for handling WebSocket connections and messages from clients.
 * 
 * @author AWMM
 */
@Component
public class ClientController extends TextWebSocketHandler {

	private final ObjectMapper mapper = new ObjectMapper();
	private final Logger logger = LoggerFactory.getLogger(ClientController.class);

	private final Map<String, Sessions> gameID2UserID2Session = new ConcurrentHashMap<>();
	private final Map<WebSocketSession, String> session2GameID = new ConcurrentHashMap<>();
	@Autowired
	private GameController gameController;
	@Autowired
	private ChatController chatController;
	public GameController getGameController() {
		return gameController;
	}

	/**
	 * Inner class representing sessions for WebSocket connections.
	 * Maintains an array of WebSocketSession objects for each user position in the
	 * game.
	 * Provides methods to manipulate the sessions array, such as adding, removing,
	 * and retrieving sessions.
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
			} else {
				return null;
			}
		}
	}

	/**
	 * Handles incoming text messages from WebSocket clients.
	 * This method is called when a client sends a text message over WebSocket.
	 * 
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
					handleAccuse(session, clientMessage);
					break;
		        case "DISPROVE":
		          handleDisprove(session, clientMessage);
		          break;
				case "HOSTGAME":
					handleHostAction(session);
					break;
				case "JOINGAME":
					handleJoinAction(session, clientMessage);
					break;
				case "ENDTURN":
					handleEndAction(session, clientMessage);
					break;

				// Add other actions
				default:
					logger.error("Unknown action received: {}", clientMessage.action());
			}
		} catch (JsonProcessingException e) {
			logger.error("Error processing incoming message from session: {}", session.getId(), e);
		}
	}

	private void handleDisprove(WebSocketSession session, ExampleMessage clientMessage) {
		// TODO Auto-generated method stub
		ExampleMessage returnMessage = new ExampleMessage(null, null, null, null, null, null, null);
		if (gameController.handleDisprove(clientMessage)) {
			returnMessage = new ExampleMessage(clientMessage.GAMEID(), clientMessage.USERID(), "SUCCESS", null, null, null, "DISPROVE");
			broadcastMessage(returnMessage, clientMessage.GAMEID());
			ExampleMessage individualMessage = new ExampleMessage(null, null, null, clientMessage.location(), clientMessage.weapon(), clientMessage.suspect(), "DisproveSuccessful");
		} else {
			returnMessage = new ExampleMessage(clientMessage.GAMEID(), clientMessage.USERID(), "FAIL", null, null, null, "DISPROVE");
			sendMessageToClient(session, returnMessage);
		}
	
	}

	/**
	 * Handles the "START" action received from a WebSocket client.
	 * This method initiates the game start procedure by calling the corresponding
	 * method in the game controller.
	 * After handling the start action, it broadcasts a message to all clients in
	 * the same game.
	 *
	 * @param clientMessage The message received from the WebSocket client.
	 */
	private void handleStartAction(WebSocketSession session, ExampleMessage clientMessage) {
		logger.info("Start message received");
		String gameID = clientMessage.GAMEID();
		int userID;
		HashMap<String, ArrayList<String>> map = gameController.handleStart(clientMessage);
		for (String player : map.keySet()) {
			for (String card : map.get(player)) {
				userID = GameController.PlayerName2UserID.get(player);
				ExampleMessage message = new ExampleMessage(gameID, userID, card, null, null, null, "CARD");
				sendMessageToClient(gameID2UserID2Session.get(gameID).get(userID), message);
			}
		}
//		Message response = new ConfirmStartMessage(true, "start");
		ExampleMessage response = new ExampleMessage(null, null, chatController.append(clientMessage.GAMEID(), "Game has started."), null, null, null, "start");
		//sendMessageToClient(session, response);
		broadcastMessage(response, clientMessage.GAMEID());
	}

	/**
	 * Handles the "SUGGEST" action received from a WebSocket client.
	 * This method processes the suggestion made by a player in the game.
	 * After handling the suggestion action, it broadcasts a message to all clients
	 * in the same game.
	 *
	 * @param session       The WebSocket session of the client.
	 * @param clientMessage The message received from the WebSocket client.
	 */
	private void handleSuggest(WebSocketSession session, ExampleMessage clientMessage) {
		String position = gameController.handleSuggest(clientMessage);
		ExampleMessage message;
		if (position != null) {
			message = new ExampleMessage(clientMessage.GAMEID(), clientMessage.USERID(), position, clientMessage.location(), clientMessage.weapon(), clientMessage.suspect(), "SUGGEST");
//			sendMessageToClient(session, message);
		}
		else {
			message = new ExampleMessage(clientMessage.GAMEID(), clientMessage.USERID(), "FAIL", clientMessage.location(), clientMessage.weapon(), clientMessage.suspect(), "SUGGEST");
		}
		broadcastMessage(message, clientMessage.GAMEID());

	}

	/**
	 * Handles the "ACCUSE" action received from a WebSocket client.
	 * This method processes the accusation made by a player in the game.
	 * After handling the accusation action, it broadcasts a message to all clients
	 * in the same game.
	 *
	 * @param clientMessage The message received from the WebSocket client.
	 */
	private void handleAccuse(WebSocketSession session, ExampleMessage clientMessage) {
		if (gameController.handleAccuse(clientMessage)) {
			ExampleMessage gameWon = new ExampleMessage(clientMessage.GAMEID(), clientMessage.USERID(), "Game Won", clientMessage.location(), clientMessage.weapon(), clientMessage.suspect(), "GAMEOVER");
			broadcastMessage(gameWon, clientMessage.GAMEID());
		} else if (gameController.activePlayers(clientMessage) <= 0) { // no winners :(		
			broadcastMessage(new NoWinMessage("Nobody Wins"), clientMessage.GAMEID());
			// initiate cleanup
		}
		else {
			sendMessageToClient(session, new AccuseFailMessage("accusefail"));
		}
	}

	/**
	 * Handles the "MOVE" action received from a WebSocket client.
	 * This method processes the move made by a player in the game.
	 * After handling the move action, it broadcasts a message to all clients in the
	 * same game.
	 *
	 * @param session       The WebSocket session of the client.
	 * @param clientMessage The message received from the WebSocket client.
	 */
	private void handleMoveAction(WebSocketSession session, ExampleMessage clientMessage) {
		String gameID = clientMessage.GAMEID();
		int userID = clientMessage.USERID();
		String location = gameController.handleMove(clientMessage);

		if (location != null) {			
			ExampleMessage ResponseSuccessMessage = new ExampleMessage(gameID, userID, "SUCCESS", location, null, null, "MOVE");
			broadcastMessage(ResponseSuccessMessage, gameID);
		}
		else {
			ExampleMessage ResponseFailMessage = new ExampleMessage(gameID, userID, "FAIL", null, null, null, "MOVE");
			sendMessageToClient(session, ResponseFailMessage);			
		}
	}

	/**
	 * Handles the "HOSTGAME" action received from a WebSocket client.
	 * This method creates a new game board state and assigns a unique game ID to
	 * it.
	 * After handling the host game action, it sends a response message to the
	 * client indicating the success or failure
	 * of the operation.
	 *
	 * @param session The WebSocket session of the client.
	 */
	private void handleHostAction(WebSocketSession session) {
		String gameID = UUID.randomUUID().toString().substring(0, 8);
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
	 * This method checks if a game is joinable and sends a response message to the
	 * client indicating the success or
	 * failure of the operation.
	 *
	 * @param session       The WebSocket session of the client.
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
			for (WebSocketSession s : sessions) {
				if (s == null) {
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

	private void handleEndAction(WebSocketSession session, ExampleMessage clientMessage){
		String gameID = clientMessage.GAMEID();
		int userID = clientMessage.USERID();

		gameController.handleEndTurn(clientMessage);
		Message response = new ExampleMessage(gameID, userID, "ENDTURN", null, null, null, "ENDTURN");
		broadcastMessage(response, gameID);
	}

	/**
	 * Called after a WebSocket connection has been closed.
	 * 
	 * @param session     The WebSocket session that has been closed.
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
	 * @param gameID  The ID of the game to broadcast the message to.
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
	 * 
	 * @param session The WebSocket session of the client.
	 * @param message The message to send.
	 */
	public void sendMessageToClient(WebSocketSession session, Message message) {
		String jsonMessage = convertToJson(message);
		try {
			session.sendMessage(new TextMessage(jsonMessage));
		} catch (IOException e) {
			logger.error("Error sending message to session: {}", session.getId(), e);
		}
	}

	/**
	 * Handles the LOGIN action from a WebSocket client for new and returning users.
	 * If gameID is set, the user is trying to join an existing game.
	 * If gameID is not set, a new game is created.
	 * 
	 * @param session       The WebSocket session representing the client
	 *                      connection.
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
			} else if (gameID != null && !gameID.isBlank()) {
				// join new game as new player
				gameID2UserID2Session.put(gameID, new Sessions());
				chatController.setChat(gameID, "New Game Created with Game ID " + gameID + "<br>");
				success = true;
			}
		}


		if (success) {
			gameID2UserID2Session.get(gameID).put(userID, session);
			session2GameID.put(session, gameID);
			gameController.addPlayer(gameID, userID);
			String append = GameController.playerNames[userID] + " has joined game.\n";
			ExampleMessage successResponseMessage = new ExampleMessage(gameID, userID, chatController.append(gameID, append), null, null, null,"LOGIN");
			broadcastMessage(successResponseMessage, gameID); // tell all users about new user
			// Send user all previous logins so they have updated state
			ArrayList<Integer> previousLogins = new ArrayList();
			Sessions sessions = gameID2UserID2Session.get(gameID);
			if (sessions != null){
				for (int i = 0; i < 6; i++){
					if (sessions.get(i) != null && i != userID){
						ExampleMessage prevLoginMessage = new ExampleMessage(gameID, i, "SUCCESS", null, null, null,"LOGIN");
						sendMessageToClient(session, prevLoginMessage);
					}
				}
			}

			logger.info("Login for GAMEID {} USERID {}", gameID, userID);
		} else {
			ExampleMessage failureResponseMessage = new ExampleMessage(gameID, userID, "FAIL", null, null, null, "LOGIN");
			sendMessageToClient(session, failureResponseMessage);
			logger.error("Failed login for GAMEID {} USERID {}", gameID, userID);
		}

	}

	/**
	 * Converts a message object to its JSON representation.
	 * 
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
