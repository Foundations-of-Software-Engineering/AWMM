package com.awmm.messageserver.board;

import com.awmm.messageserver.player.Player;

public interface Location {

//	public Player getPlayer() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	public abstract boolean available();
//	public abstract boolean isAdjacent(Location location);
//	public abstract boolean takePlayer(Player player);
//	public abstract boolean removePlayer(Player player);
	
	public boolean setPlayer(Player player);
	public Player getPlayer();
	public Player removePlayer(Player player);
	
}
