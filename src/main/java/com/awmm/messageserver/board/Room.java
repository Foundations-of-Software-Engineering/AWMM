package com.awmm.messageserver.board;

import java.util.HashMap;

import com.awmm.messageserver.player.Player;

public class Room implements Location{

	private String name;
	private HashMap<String, Player> players;
	
	public Room(String name) {
		super();
		this.name = name;
		this.players = new HashMap<String, Player>();
	}

	/* Getters Setters Start */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	/* Getters Setters End */
	
	public boolean removePlayer(Player player) {
		return players.remove(player.getName()) != null;
	}

	@Override
	public boolean setPlayer(Player player) {
		if (player == null) {return false;}
		players.put(player.getName(), player);
		return true;
	}

	@Override
	public Player getPlayer(String playerName) {
		return players.get(playerName);
	}

	@Override
	public String toString() {
		return "[" + name + ": " + players.values() + "]";
	}

}

