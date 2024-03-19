package com.awmm.messageserver.location;

import java.util.Objects;

import com.awmm.messageserver.player.Player;

public class Hallway extends Location {

	private Player player;
	private Room room1;
	private Room room2;
	private String name;
	
	public Hallway(Player player, Room room1, Room room2, String name) {
		super();
		this.player = player;
		this.room1 = room1;
		this.room2 = room2;
		this.name = name;
	}

	public Hallway(Room room1, Room room2, String name) {
		super();
		this.room1 = room1;
		this.room2 = room2;
		this.name = name;
	}

	@Override
	public boolean available() {
		return player == null;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public boolean isAdjacent(Location location) {
		return room1.equals(location) || room2.equals(location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(player, room1, room2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hallway other = (Hallway) obj;
		return other.name.equals(name);
	}

	@Override
	public boolean takePlayer(Player player) {
		boolean ret = false;
		if (player.getLocation().equals(room1) && room1.removePlayer(player)) {
			this.player = player;
			ret = true;
		}
		else if (player.getLocation().equals(room2) && room2.removePlayer(player)) {
			this.player = player;
			ret = true;
		}
		return ret;
	}

	@Override
	public boolean removePlayer(Player player) {
		boolean ret = false;
		if (player.equals(player)) {
			player = null;
			ret = true;
		}
		return ret;
	}
	
}
