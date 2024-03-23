package com.awmm.messageserver.player;

import com.awmm.messageserver.board.Location;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Player {

	@Id
	private long id;
	private long gameId;
	private String name;
	private Location location;
	
	public Player() {
	}

	public Player(long id, long gameId, String name) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.name = name;

	}
	
	public Player(long id, long gameId, String name, Location location) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.name = name;
		this.location = location;
	}
	
	public Player(Player player) {
		super();
		this.id = player.getId();
		this.gameId = player.getGameId();
		this.name = player.getName();
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", gameId=" + gameId + ", name=" + name + ", location=" + location + "]";
	}
	
}
