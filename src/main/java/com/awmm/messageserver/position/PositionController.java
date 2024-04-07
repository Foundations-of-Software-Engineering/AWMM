package com.awmm.messageserver.position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.awmm.messageserver.board.Board;

@Component
public class PositionController {

	@Autowired
	private PositionRepository repository;
	
	public PositionController() {
	}
	
	public Position getPosition(String gameID, String player) {
		int userID = getUserIDFromName(player);
		if (userID != -1) {
			return getPosition(gameID, userID);
		}
		return null;
	}

	public Position getPosition(String gameID, int userID) {
		return repository.getReferenceById(new PositionID(gameID, userID));
	}
	
	public Position savePosition(Position position) {
		return repository.save(position);
	}
	
	public Position savePosition(String gameID, int userID, int row, int col) {
		return (userID >= 0 && userID < 6) ? repository.save(new Position(gameID, userID, row, col)) : null; 
	}
	
	public void delete(String gameID) {
		for (int i = 0; i < 6; ++i) repository.deleteById(new PositionID(gameID, i));
	}
	
	private int getUserIDFromName(String player) {
		switch (player) {
		case Board.ProfessorPlumName: { return 0; }
		case Board.  MissScarletName: { return 1; }
		case Board.   ColMustardName: { return 2; }
		case Board.   MrsPeacockName: { return 3; }
		case Board.      MrGreenName: { return 4; }
		case Board.     MrsWhiteName: { return 5; }
		default: return -1;
		}
	}
}
