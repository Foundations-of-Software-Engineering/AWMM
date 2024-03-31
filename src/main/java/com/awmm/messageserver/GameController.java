package com.awmm.messageserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import com.awmm.messageserver.board.Board;
import com.awmm.messageserver.board.Board.PlayerEnum;

/**
 * Controller class for communicating with the game server.
 * @author AWMM
 */
@Controller
public class GameController {

	HashMap<String, Board> boardStates;

	private final Logger logger = LoggerFactory.getLogger(GameController.class);
	private final Board.PlayerEnum[] playerEnums = {
			Board.PlayerEnum.ProfessorPlum,
			Board.PlayerEnum.MissScarlet  ,  
			Board.PlayerEnum.ColMustard   ,  
			Board.PlayerEnum.MrsPeacock   ,  
			Board.PlayerEnum.MrGreen      ,  
			Board.PlayerEnum.MrsWhite     	 
			};
	
	public GameController() {
		boardStates = new HashMap<String, Board>();
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
    
}