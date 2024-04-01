package com.awmm.messageserver;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.awmm.messageserver.board.Board;
import com.awmm.messageserver.board.Board.PlayerEnum;

/**
 * Controller class for communicating with the game server.
 * @author AWMM
 */
@Controller
public class GameController {

	private HashMap<String, Board> boardStates;

	private final Logger logger;
	private final Board.PlayerEnum[] playerEnums; 
	
	public GameController() {
		this.boardStates = new HashMap<String, Board>();
		this.logger = LoggerFactory.getLogger(GameController.class);
		this.playerEnums = PlayerEnum.values();
	}
	
	public void createBoardState(String gameID) {
		boardStates.put(gameID, new Board(gameID));
	}
	
	public void handleMove(Message clientMessage) {
		String gameID   = clientMessage.GAMEID();
		int    userID   = clientMessage.USERID();
		String location = clientMessage.location().toUpperCase();
		Board.Direction direction;
		if (isValid(gameID, userID)) {
			switch(location) {
			case "UP":
				direction = Board.Direction.UP;
				break;
			case "DOWN":
				direction = Board.Direction.DOWN;
				break;
			case "LEFT":
				direction = Board.Direction.LEFT;
				break;
			case "RIGHT":
				direction = Board.Direction.RIGHT;
				break;
			default: {
	            logger.error("Error processing direction: direction received: {0}", location);
				return;
			}
			}
			boardStates.get(gameID).movePlayer(playerEnums[userID], direction);
		}
		else {
			logger.error("Invalid gameId: {0} or userID{1}", gameID, userID);
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
		
		Board board = boardStates.get(gameId);

		Board.PlayerEnum playerEnum = null;
		
		for (PlayerEnum findPlayerEnum : playerEnums) {
			if (suspect.equals(findPlayerEnum.name)) {
				playerEnum = findPlayerEnum;
				break;
			}
		}
		
		if (playerEnum == null) {
            logger.error("Error: could not find playerEnum from suspect: {0}", suspect);
		}
		
		Board.RoomEnum roomEnum = board.getRoomEnum(playerEnums[userId]);
		
		board.movePlayer(playerEnum, roomEnum);
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