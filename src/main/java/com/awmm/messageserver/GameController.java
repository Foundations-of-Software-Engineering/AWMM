package com.awmm.messageserver;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.awmm.messageserver.board.Board;

/**
 * Controller class for communicating with the game server.
 * @author AWMM
 */
@Controller
public class GameController {

	private HashMap<String, Board> boardStates;

	private final Logger logger;
	private static final String[] playerNames = {
		Board.ProfessorPlumName,
		Board.MissScarletName,
		Board. ColMustardName,
		Board. MrsPeacockName,
		Board.    MrGreenName,
		Board.   MrsWhiteName
	}; 
	
	public GameController() {
		this.boardStates = new HashMap<String, Board>();
		this.logger = LoggerFactory.getLogger(GameController.class);
	}
	
	public void createBoardState(String gameID) {
		boardStates.put(gameID, new Board(gameID));
	}
	
	public void handleMove(Message clientMessage) {
		String gameID   = clientMessage.GAMEID();
		int    userID   = clientMessage.USERID();
		String location = clientMessage.location();
		
		if (isValid(gameID, userID) && location != null) {
			boardStates.get(gameID).movePlayer(playerNames[userID], location.toUpperCase());
		}
		else {
			logger.error("Invalid gameId: {0} or userID: {1} or location: {2}", gameID, userID, location);
		}
	}
	
	public void handleSuggest(Message clientMessage) {
		String     gameId   = clientMessage.GAMEID()  ;
		int        userId   = clientMessage.USERID()  ;
		String     suspect  = clientMessage.suspect() ;
		
		if (!isValid(gameId, userId)) {
            logger.error("Error processing gameId: {0} or userId: {1}", gameId, userId);
			return;
		}
		
		if (suspect == null) {
            logger.error("Suspect is null");
			return;
		}
		
		boardStates.get(gameId).handleSuggest(playerNames[userId], suspect);

	}
	
	private boolean isValid(String gameId, int userId) {
		return boardStates.containsKey(gameId) && userId >= 0 && userId <= 5;
	}
	
	public String getBoardState(String gameID) {
		Board board = boardStates.get(gameID);
		if (board != null) {
			return board.toString();
		}
		else return "";
	}

	public void handleStart(Message clientMessage) {
		// TODO Auto-generated method stub
		String gameID = clientMessage.GAMEID();
		Board board = boardStates.get(gameID);
		if (board != null) {
			board.start();
		}
		
	}
    
}