package com.awmm.messageserver.location;

import java.util.ArrayList;

import com.awmm.messageserver.player.Player;

public class Room extends Location {

	private String name;
	private ArrayList<Player> players;
	private ArrayList<Hallway> hallways;
	
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
	
}

