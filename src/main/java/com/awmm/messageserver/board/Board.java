package com.awmm.messageserver.board;

import java.util.Arrays;

import com.awmm.messageserver.player.Player;

public class Map {
	
	public static final int ROW_SIZE = 5;
	public static final int COL_SIZE = 5;
	
	public static final String PLUM_NAME    = "Professor Plum";
	public static final String SCARLET_NAME = "Miss Scarlet";
	public static final String MUSTARD_NAME = "Col. Mustard";
	public static final String PEACOCK_NAME = "Mrs. Peacock";
	public static final String GREEN_NAME   = "Mrs. Green";
	public static final String WHITE_NAME   = "Mrs. White";
	
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
		Study       ,
		Hall        ,
		Lounge      ,
		Library     ,
		BilliardRoom,
		DiningRoom  ,
		Conservatory,
		Ballroom    , 
		Kitchen
	}
	
	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		DIAGONAL
	}
	
	static public class Position {
		Position() {
			row = -1;
			col = -1;
		}
		Position(int row, int col) {
			this.row = row;
			this.col = col;
		}
		private int row;
		private int col;
	}
	
	private Location[][] grid = new Location[ROW_SIZE][COL_SIZE];
	static private final Position STUDY         = new Position(0,0);
	static private final Position HALL          = new Position(0,2);
	static private final Position LOUNGE        = new Position(0,4);
	static private final Position LIBRARY       = new Position(2,0);
	static private final Position BILLIARD_ROOM = new Position(2,2);
	static private final Position DINING_ROOM   = new Position(2,4);
	static private final Position CONSERVATORY  = new Position(4,0);
	static private final Position BALLROOM      = new Position(4,2);
	static private final Position KITCHEN       = new Position(4,3);
	
	private Position plum   ;
	private Position scarlet;
	private Position mustard;
	private Position peacock;
	private Position green  ;
	private Position white  ;
	
	public Map(long gameId) {
		super();
		this.gameId = gameId;
		plum    = new Position();
		scarlet = new Position();
		mustard = new Position();
		peacock = new Position();
		green   = new Position();
		white   = new Position();
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
		for (int i = 0; i < ROW_SIZE; ++i) {
			for (int j = 0; j < COL_SIZE; ++j) {
				if (grid[i][j] == null) {					
					grid[i][j] = new Hallway();
				}
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
	
	/* To be used for normal movement */
	public boolean movePlayer(PlayerName playerName, Direction direction) {
		boolean  ret = false;
		Position newPosition = null;
		Position oldPosition = null;
		String key = null;
		switch(playerName) {
			case ProfessorPlum:
			{
				oldPosition = plum;
				key = PLUM_NAME;
				break;
			}
			case MissScarlet  :
			{
				oldPosition = scarlet;
				key = SCARLET_NAME;
				break;
			}
			case ColMustard   :
			{
				oldPosition = mustard;
				key = MUSTARD_NAME;
				break;
			}
			case MrsPeacock   :
			{
				oldPosition = peacock;
				key = PEACOCK_NAME;
				break;
			}
			case MrGreen      :
			{
				oldPosition = green;
				key = GREEN_NAME;
				break;
			}
			case MrsWhite     :
			{
				oldPosition = white;
				key = WHITE_NAME;
				break;
			} 
			default: 
			{
				/*Log Bad PlayerName Enum*/
				return false;
			}
		}
		
		newPosition = new Position(oldPosition.row, oldPosition.col);
		
		switch (direction) {
			case UP:
			{
				if (oldPosition.row-1 >= 0) {
					--newPosition.row;
				}
				break;
			}
			case DOWN:
			{
				if (oldPosition.row+1 < ROW_SIZE) {
					++newPosition.row;
				}
				break;
			}
			case LEFT:
			{
				if (oldPosition.col-1 >= 0) {
					--newPosition.col;
				}
				break;
			}
			case RIGHT:
			{
				if (oldPosition.col+1 < COL_SIZE) {
					++newPosition.col;
				}
				break;
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
				break;
			}
			default: {return false;} 
		}
		
		return move(key, oldPosition, newPosition);				
	}
	
	/* To be used for suggestions and accusations */
	public boolean movePlayer(PlayerName playerName, RoomName roomName) {
		Position oldPosition = null;
		Position newPosition = null;
		String   key         = null;
		
		switch(playerName) {
		case ProfessorPlum:{oldPosition = plum   ; key = PLUM_NAME   ; break;}
		case MissScarlet  :{oldPosition = scarlet; key = SCARLET_NAME; break;}
		case ColMustard   :{oldPosition = mustard; key = MUSTARD_NAME; break;}
		case MrsPeacock   :{oldPosition = peacock; key = PEACOCK_NAME; break;}
		case MrGreen      :{oldPosition = green  ; key = GREEN_NAME  ; break;}
		case MrsWhite     :{oldPosition = white  ; key = WHITE_NAME  ; break;}
		default: {return false;}
		}
		
		switch(roomName) {
		case Study       : {newPosition = STUDY        ; break;}
		case Hall        : {newPosition = HALL         ; break;}
		case Lounge      : {newPosition = LOUNGE       ; break;}
		case Library     : {newPosition = LIBRARY      ; break;}
		case BilliardRoom: {newPosition = BILLIARD_ROOM; break;}
		case DiningRoom  : {newPosition = DINING_ROOM  ; break;}
		case Conservatory: {newPosition = CONSERVATORY ; break;}
		case Ballroom    : {newPosition = BALLROOM     ; break;}
		case Kitchen     : {newPosition = KITCHEN      ; break;}
		default: {return false;}
		}
		return move(key, oldPosition, newPosition);
	}
	
	private Location getLocation(Position position) {
		if (position.row >= 0 && position.row < ROW_SIZE && position.col >= 0 && position.col < COL_SIZE) {			
			return grid[position.row][position.col];
		}
		return null;
	}
	
	private boolean move(String key, Position oldPosition, Position newPosition) {
		boolean ret = false;
		Location oldLocation = getLocation(oldPosition);
		Location newLocation = getLocation(newPosition);
		if (oldLocation != null && newLocation != null)
		{		
			Player player = oldLocation.getPlayer(key);
			if (oldLocation.removePlayer(player) && newLocation.setPlayer(player)) {
				oldPosition.row = newPosition.row;
				oldPosition.col = newPosition.col;
				ret = true;
			}
		}
		return ret;
	}

	@Override
	public String toString() {
		String toString = "Map [gameId=" + gameId + ", grid=" + Arrays.toString(grid) + ", plum=" + plum + ", scarlet=" + scarlet
				+ ", mustard=" + mustard + ", peacock=" + peacock + ", green=" + green + ", white=" + white + "]";
		toString += "\n";
		for (int row = 0; row < ROW_SIZE; ++row) {
			for (int col = 0; col < COL_SIZE; ++col) {
				toString += grid[row][col].toString();
			}
			toString += "\n";
		}
		return toString;
	}
	
	
}
