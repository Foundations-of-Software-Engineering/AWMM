package com.awmm.messageserver.location;

import com.awmm.messageserver.player.Player;

public abstract class Location {

	public abstract boolean available();
	public abstract boolean isAdjacent(Location location);
	public abstract boolean takePlayer(Player player);
	public abstract boolean removePlayer(Player player);
}
