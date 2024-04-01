package com.awmm.messageserver.player;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Player {

	@Id
	private int id;
	private String gameId;
	private String name;
	
	private ArrayList<String> hand;
	
	public Player() {
	}

	public Player(int id, String gameId, String name) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.name = name;
		this.hand = new ArrayList<>();

	}
	
	public Player(Player player) {
		super();
		this.id = player.getId();
		this.gameId = player.getGameId();
		this.name = player.getName();
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String toString = name;
		toString += " [";
		if (hand.size() == 0) {
			toString += "]";
		}
		else {			
			for (String card : hand) {
				if (card != null) {
					toString += card + ",";
				}
			}
			toString = toString.substring(0, toString.length()-1) + "]";
		}
		return toString;	
	}

	public void receiveCard(String card) {
		hand.add(card);
	}
	
}
