package com.awmm.messageserver.player;

import com.awmm.messageserver.location.Location;

public class PlayerController {

	public boolean move (Player player, Location location) {
		return location.takePlayer(player);
	}
	
	
}
