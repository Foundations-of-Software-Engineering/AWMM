package com.awmm.messageserver;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.awmm.messageserver.board.Board;
import com.awmm.messageserver.cards.CardsController;
import com.awmm.messageserver.messages.ExampleMessage;
import com.awmm.messageserver.position.PositionController;
import com.awmm.messageserver.positions.PositionsController;

/**
 * Controller class for communicating with the game server.
 * @author AWMM
 */
@Controller
public class GameController {

	// gameId to board state
	private final HashMap<String, Board> boardStates;
	
//	private final CardsController cardsController;
//	private final PositionsController positionsController;
	
	
	@Autowired
	private PositionController positionController;
	@Autowired
	private CardsController cardsController;

	private final Logger logger;
	private static final String[] playerNames = {
		Board.    MissScarletName,
		Board.  ProfessorPlumName,
		Board.     ColMustardName,
		Board.     MrsPeacockName,
		Board.        MrGreenName,
		Board.       MrsWhiteName
	};

	int losers = 0;

	/**
	 * Constructor for GameController.
	 */
	public GameController() {
		this.boardStates = new HashMap<>();
		this.logger = LoggerFactory.getLogger(GameController.class);
//		this.cardsController = new CardsController();
//		this.positionsController = new PositionsController();
	}

	/**
	 * Creates a new board state for the specified game ID.
	 *
	 * @param gameID The ID of the game.
	 * @return true if the board state is created successfully, false otherwise.
	 */
	public boolean createBoardState(String gameID) {
		if (boardStates.containsKey(gameID)){
			return false;
		}
		else {
			boardStates.put(gameID, new Board(gameID, positionController, cardsController));
			return true;
		}
	}
	
	// boolean 
	public void handleMove(ExampleMessage clientMessage) {
		String gameID   = clientMessage.GAMEID();
		int    userID   = clientMessage.USERID();
		String location = clientMessage.location();
		
		if (isValid(gameID, userID) && location != null) {
			boardStates.get(gameID).movePlayer(playerNames[userID], location.toUpperCase());
		}
		else {
			logger.error("Invalid gameId: {} or userID: {} or location: {}", gameID, userID, location);
		}
	}


	public boolean handleSuggest(ExampleMessage clientMessage) {
		String     gameId   = clientMessage.GAMEID()  ;
		int        userId   = clientMessage.USERID()  ;
		String     suspect  = clientMessage.suspect() ;
		String     weapon   = clientMessage.weapon()  ;
		
		if (!isValid(gameId, userId)) {
            logger.error("Error processing gameId: {} or userId: {}", gameId, userId);
		}
		else if (suspect == null) {
            logger.error("Suspect should not be null when making suggestion.");
		}
		else if (weapon == null) {
			logger.error("Weapon should not be null when making suggestion.");
		}
		else if (!cardsController.hasSuggestion(gameId)){			
			String roomName = boardStates.get(gameId).handleSuggest(playerNames[userId], suspect);
			if (roomName != null) {				
				cardsController.setSuggestion(gameId, weapon, suspect, roomName);
				return true;
			}
		}
		return false;
	}

	public void handleAccuse(ExampleMessage clientMessage) {
		Board board = boardStates.get(clientMessage.GAMEID());

		String     gameId   = clientMessage.GAMEID()  ;
		int        userId   = clientMessage.USERID()  ;
		String     suspect  = clientMessage.suspect() ;
		String     weapon  = clientMessage.weapon() ;
		String     location  = clientMessage.location() ;

		if (!isValid(gameId, userId)) {
			logger.error("Error processing gameId: {} or userId: {}", gameId, userId);
			return;
		}

		if (suspect == null) {
			logger.error("Suspect is null");
			return;
		}

		if (weapon == null) {
			logger.error("Weapon is null");
			return;
		}

		if (location == null) {
			logger.error("Location is null");
			return;
		}

		if (clientMessage.location() == board.getWinningCards()[0]
			&& clientMessage.weapon() == board.getWinningCards()[1]
			&& clientMessage.suspect() == board.getWinningCards()[2]) {
			logger.info("Accusation matches winning cards. Game over.");
			board.setStarted(false);
		} else {
			// invalid integer will prevent user from making future decisions
			userId = 999999;

			losers += 1;
			if (losers == board.getPlayers().size()) {
				logger.info("Every accuser was incorrect. Game over");
				board.setStarted(false);
			}
		}


	}
	
	private boolean isValid(String gameId, int userId) {
		return boardStates.containsKey(gameId) && userId >= 0 && userId <= 5;
	}

	/**
	 * Checks if the game with the specified ID is joinable.
	 *
	 * @param gameID The ID of the game.
	 * @return true if the game is joinable, false otherwise.
	 */
	public boolean isJoinable(String gameID) {
		Board board = boardStates.get(gameID);

		if (board.getStarted() /* must also check number of players */) {
			return false;
		}
		return true;
	}

	/**
	 * Retrieves the board state for the specified game ID.
	 *
	 * @param gameID The ID of the game.
	 * @return The board state as a string.
	 */
	public String getBoardState(String gameID) {
		Board board = boardStates.get(gameID);
		if (board != null) {
			return board.toString();
		}
		else return "Cannot find Board with Game ID: " + gameID;
	}

	// TODO
	// if called before handleLogin it will fail
	// could return boolean to denote failure or success
	public void handleStart(ExampleMessage clientMessage) {
		// TODO Auto-generated method stub
		String gameID = clientMessage.GAMEID();
		Board board = boardStates.get(gameID);
		if (board != null) {
			board.start();
		}
	}

//	public void setCards(Cards cards) {
//		// TODO Auto-generated method stub
//		Map<String, String> map = cardsController.getCardsMap(cards);
//		String gameID = map.remove("gameID");
//		if (boardStates.containsKey(gameID)) {
//			boardStates.get(gameID).setCards(map);
//		}
//	}

//	public void setPositions(Positions positions) {
//		// TODO Auto-generated method stub
//		Map<String, Integer[]> map = positionsController.getPositionsMap(positions);
//		String gameID = positions.getGameID();
//		if (boardStates.containsKey(gameID)) {
//			boardStates.get(gameID).setPositions(map);
//		}
//		
//	}
	
	public void addPlayer(String gameID, int userID) {
		boardStates.get(gameID).addPlayer(playerNames[userID]);
	}
    
}