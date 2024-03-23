package com.awmm.messageserver.board;

import java.util.ArrayList;

import com.awmm.messageserver.player.Player;

public class Room implements Location{

	private String name;
	private ArrayList<Player> players;
	private ArrayList<Hallway> hallways;
	
	public Room(String name) {
		super();
		this.name = name;
		this.hallways = hallways;
	}
	
	public Room(String name, ArrayList<Player> players, ArrayList<Hallway> hallways) {
		super();
		this.name = name;
		this.players = players;
		this.hallways = hallways;
	}

	public boolean contains(Player player) {
		for (Player it : players) {
			if (it.equals(player)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean available() {
		return true;
	}

	@Override
	public boolean isAdjacent(Location location) {
		for (Hallway hallway : hallways) {
			if (hallway.equals(location))
				return true;
		}
		return false;
	}
	
	public boolean removePlayer(Player player) {
		for (Player it : players) {
			if (it.equals(player)) {
				players.remove(player);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean takePlayer(Player player) {
		if (player.getLocation().equals(this)) {
			return true;
		}

		for (Hallway hallway : hallways) {
			if (hallway.getPlayer().equals(player) && hallway.removePlayer(player)) {
				hallways.remove(player);
				return true;
			}
		}
		
		return false;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<Hallway> getHallways() {
		return hallways;
	}

	public void setHallways(ArrayList<Hallway> hallways) {
		this.hallways = hallways;
	}

	@Override
	public boolean setPlayer(Player player) {
		return true;
		
	}
	
}

