package com.awmm.messageserver.chat;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Chat {
	
	@Id 
	String gameID;
	
	String text;

	public Chat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Chat(String gameID, String text) {
		super();
		this.gameID = gameID;
		this.text = text;
	}

	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		return Objects.hash(gameID, text);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chat other = (Chat) obj;
		return Objects.equals(gameID, other.gameID) && Objects.equals(text, other.text);
	}
	
}
