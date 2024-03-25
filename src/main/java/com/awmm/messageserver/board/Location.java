package com.awmm.messageserver.board;

import com.awmm.messageserver.player.Player;

public interface Location {

	public boolean setPlayer(Player player);
	public Player getPlayer(String playerName);
	public boolean removePlayer(Player player);
	
}
