package com.awmm.messageserver.board;

import com.awmm.messageserver.player.Player;

public class Map {
	
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
	
	private class Position {
		private int row = -1;
		private int col = -1;
	}
	
	private Location[][] grid = new Location[5][5];
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
		for (int i = 1; i < 5; i+=2) {
			for (int j = 1; j < 5; j+=2) {
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
	
	public boolean movePlayerUp(PlayerName playerName) {
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
				if (grid[peacock.row-1][peacock.col].setPlayer(grid[peacock.row][peacock.col].getPlayer())) {
					--peacock.row;
					ret = true;
				}
				break;
			}
			case MrGreen      :
			{
				if (grid[green.row-1][green.col].setPlayer(grid[green.row][green.col].getPlayer())) {
					--green.row;
					ret = true;
				}
				break;
			}
			case MrsWhite     :
			{
				if (grid[white.row-1][white.col].setPlayer(grid[white.row][white.col].getPlayer())) {
					--white.row;
					ret = true;
				}
				break;
			} 
			default: 
			{
				/*Log Bad PlayerName Enum*/
			}
		}
		return ret;
		
	}
	
}
