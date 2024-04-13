package com.awmm.messageserver.position;

import jakarta.persistence.Embeddable;

@Embeddable
public class PositionID {
	String gameID;
	int userID;
	
	public PositionID() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PositionID(String gameID, int userID) {
		this.gameID = gameID;
		this.userID = userID;
	}
}