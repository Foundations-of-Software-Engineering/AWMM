package com.awmm.messageserver.player;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Player {

	@Id
	private long id;
	private long gameId;
	private String name;
	private String location;
	
	public Player() {
	}

	public Player(long id, long gameId, String name, String location) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.name = name;
		this.location = location;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", gameId=" + gameId + ", name=" + name + ", location=" + location + "]";
	}
	
}
