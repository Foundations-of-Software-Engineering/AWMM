package com.awmm.messageserver.board;

import com.awmm.messageserver.player.Player;

public class Map {
	
	public static final int ROW_SIZE = 5;
	public static final int COL_SIZE = 5;
	
	private long gameId;

	public enum PlayerName {
		ProfessorPlum,
		MissScarlet  ,
		ColMustard   ,
		MrsPeacock   ,
		MrGreen      ,
		MrsWhite   	
	}
	
	public enum RoomName {
		Study,
		Hall,
		Lounge,
		Library,
		BilliardRoom,
		DiningRoom,
		Conservatory,
		Ballroom, 
		Kitchen
	}
	
	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		DIAGONAL
	}
	
	private class Position {
		private int row = -1;
		private int col = -1;
	}
	
	private Location[][] grid = new Location[ROW_SIZE][COL_SIZE];
	private Position plum   ;
	private Position scarlet;
	private Position mustard;
	private Position peacock;
	private Position green  ;
	private Position white  ;
	
	public Map() {
		super();
		//Rooms
		grid[0][0] = new Room("Study");         
		grid[0][2] = new Room("Hall");          
		grid[0][4] = new Room("Lounge");        
		grid[2][0] = new Room("Library");       
		grid[2][2] = new Room("Billiard Room"); 
		grid[2][4] = new Room("Dining Room");   
		grid[4][0] = new Room("Conservatory");  
		grid[4][2] = new Room("Ballroom");      
		grid[4][4] = new Room("Kitchen");       
		//Hallways
		for (int i = 1; i < ROW_SIZE; i+=2) {
			for (int j = 1; j < COL_SIZE; j+=2) {
				grid[i][j] = new Hallway();
			}
		}
	}
	
	public Player addPlayer(PlayerName playerName) {
		switch(playerName) {
			case ProfessorPlum: 
			{
				Player player = new Player(0l, gameId, "Professor Plum");
				plum.row = 1;
				plum.col = 0;
				grid[1][0].setPlayer(player); 
				return player;
			}
			case MissScarlet  : 
			{
				Player player = new Player(1l, gameId, "Miss Scarlet"); 
				scarlet.row = 0;
				scarlet.col = 3;
				grid[0][3].setPlayer(player); 
				return player;
			}
			case ColMustard   : 
			{
				Player player = new Player(2l, gameId, "Col. Mustard");
				mustard.row = 1;
				mustard.col = 4;
				grid[1][4].setPlayer(player);
				return player;
			}
			case MrsPeacock   : 
			{
				Player player = new Player(3l, gameId, "Mrs. Peacock");
				peacock.row = 3;
				peacock.col = 0;
				grid[3][0].setPlayer(player); 
				return player;
			}
			case MrGreen      : 
			{
				Player player = new Player(4l, gameId, "Mrs. Green");
				green.row = 4;
				green.col = 1;
				grid[4][1].setPlayer(player); 
				return player;
			}
			case MrsWhite     : 
			{
				Player player = new Player(5l, gameId, "Mrs. White");
				white.row = 4;
				white.col = 3;
				grid[4][3].setPlayer(player); 
				return player;}
			default: {/*Log Bad PlayerName Enum*/}	
		}
		return null;
	}
	
	public boolean movePlayer(PlayerName playerName, Direction direction) {
		boolean ret = false;
		Position newPosition;
		Position oldPosition;
		
		switch(playerName) {
			case ProfessorPlum:
			{
				oldPosition = plum;
				break;
			}
			case MissScarlet  :
			{
				oldPosition = scarlet;
				break;
			}
			case ColMustard   :
			{
				oldPosition = mustard;
				break;
			}
			case MrsPeacock   :
			{
				oldPosition = peacock;
				break;
			}
			case MrGreen      :
			{
				oldPosition = green;
				break;
			}
			case MrsWhite     :
			{
				oldPosition = white;
				break;
			} 
			default: 
			{
				/*Log Bad PlayerName Enum*/
				return false;
			}
		}
		
		newPosition = oldPosition;
		Player player = null;
		
		switch (direction) {
			case UP:
			{
				if (oldPosition.row-1 >= 0) {
					--newPosition.row;
					player = grid[oldPosition.row][oldPosition.col].getPlayer();					
				}
				break;
			}
			case DOWN:
			{
				if (oldPosition.row+1 < ROW_SIZE) {
					++newPosition.row;
					player = grid[oldPosition.row][oldPosition.col].getPlayer();
				}
			}
			case LEFT:
			{
				if (oldPosition.col-1 >= 0) {
					--newPosition.col;
					player = grid[oldPosition.row][oldPosition.col].getPlayer();
				}
			}
			case RIGHT:
			{
				if (oldPosition.col+1 < COL_SIZE) {
					++newPosition.col;
					player = grid[oldPosition.row][oldPosition.col].getPlayer(); 
				}
			}
			case DIAGONAL:
			{
				if (oldPosition.row == 0 && oldPosition.col == 0) {
					newPosition.row = 4;
					newPosition.col = 4;
				}
				else if (oldPosition.row == 0 && oldPosition.col == 4) {
					newPosition.row = 4;
					newPosition.col = 0;
				}
				else if (oldPosition.row == 4 && oldPosition.col == 0) {
					newPosition.row = 0;
					newPosition.col = 4;
				}
				else if (oldPosition.row == 4 && oldPosition.col == 4) {
					newPosition.row = 0;
					newPosition.col = 0;
				}
				if (oldPosition != newPosition) {
					player = grid[oldPosition.row][oldPosition.col].getPlayer(); 
				}
			}
		}
		
		if (player != null && grid[newPosition.row][newPosition.col].setPlayer(player)) {
			grid[oldPosition.row][oldPosition.col].removePlayer(player);
			ret = true;
		}
		
		return ret;
	}
	
}
