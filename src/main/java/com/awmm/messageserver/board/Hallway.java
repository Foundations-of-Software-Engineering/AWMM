package com.awmm.messageserver.board;

import com.awmm.messageserver.player.Player;

public class Hallway implements Location {

	private Player player;
	
	public Hallway() {
		super();
		player = null;
	}
	
	public Player getPlayer(String playerName) {
		// Check?
		return player;
	}

	public boolean setPlayer(Player player) {
		if (this.player == null) {
			this.player = player;
			return true;
		}
		return false;
	}

	@Override
	public boolean removePlayer(Player player) {
		if (player != null && this.player != null && this.player.equals(player)) {
			this.player = null;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[Hallway, player=" + player + "]";
	}
	
}
