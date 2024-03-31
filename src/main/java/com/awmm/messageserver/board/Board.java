package com.awmm.messageserver.board;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awmm.messageserver.player.Player;

public class Board {
	
	public static final int ROW_SIZE = 5;
	public static final int COL_SIZE = 5;
	
	private String gameId;

	public enum PlayerEnum {
		ProfessorPlum(0, "Professor Plum"),
		MissScarlet  (1, "Miss Scarlet"  ),
		ColMustard   (2, "Col. Mustard"  ),
		MrsPeacock   (3, "Mrs. Peacock"  ),
		MrGreen      (4, "Mrs. Green"    ),
		MrsWhite   	 (5, "Mrs. White"    );
		
		public final String name;
		public final int    id  ;
		
		PlayerEnum(int id, String name) { 
			this.id = id; 
			this.name = name; 
		}
	}
	
	public enum RoomEnum {
		Study       (0,0),
		Hall        (0,2),
		Lounge      (0,4),
		Library     (2,0),
		BilliardRoom(2,2),
		DiningRoom  (2,4),
		Conservatory(4,0),
		Ballroom    (4,2), 
		Kitchen     (4,3);
		
		private final Position position;
		
		RoomEnum(int row, int col) {
			this.position = new Position(row, col);
		}
	}
	
	RoomEnum[] roomEnums = {
			Board.RoomEnum.Study        ,       
			Board.RoomEnum.Hall         ,
			Board.RoomEnum.Lounge       ,
			Board.RoomEnum.Library      ,
			Board.RoomEnum.BilliardRoom ,
			Board.RoomEnum.DiningRoom   ,
			Board.RoomEnum.Conservatory ,
			Board.RoomEnum.Ballroom     ,
			Board.RoomEnum.Kitchen      
	};
	
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
		@Override
		public int hashCode() {
			return Objects.hash(col, row);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Position other = (Position) obj;
			return col == other.col && row == other.row;
		}
		Position(int row, int col) {
			this.row = row;
			this.col = col;
		}
		private int row;
		private int col;
	}
	
	private Location[][] grid = new Location[ROW_SIZE][COL_SIZE];
	
	private Position plum   ;
	private Position scarlet;
	private Position mustard;
	private Position peacock;
	private Position green  ;
	private Position white  ;
	
	private final Logger logger = LoggerFactory.getLogger(Board.class);

	public Board(String gameId) {
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
	
	private Player addPlayer(PlayerEnum playerEnum) {
		Player player = new Player(playerEnum.id, gameId, playerEnum.name);
		switch(playerEnum) {
			case ProfessorPlum: 
			{
				plum.row = 1;
				plum.col = 0;
				grid[1][0].setPlayer(player); 
				break;
			}
			case MissScarlet  : 
			{
				scarlet.row = 0;
				scarlet.col = 3;
				grid[0][3].setPlayer(player); 
				break;
			}
			case ColMustard   : 
			{
				mustard.row = 1;
				mustard.col = 4;
				grid[1][4].setPlayer(player);
				break;
			}
			case MrsPeacock   : 
			{
				peacock.row = 3;
				peacock.col = 0;
				grid[3][0].setPlayer(player);
				break;
			}
			case MrGreen      : 
			{
				green.row = 4;
				green.col = 1;
				grid[4][1].setPlayer(player);
				break; 
			}
			case MrsWhite     : 
			{
				white.row = 4;
				white.col = 3;
				grid[4][3].setPlayer(player);
				break;
			}
			default: {/*Log Bad Enum*/}	
		}
		return player;
	}
		
	/* To be used for normal movement */
	public boolean movePlayer(PlayerEnum playerEnum, Direction direction) {
		Position newPosition = null;
		Position oldPosition = null;
		
		if (playerEnum == null || direction == null) {
            logger.error("Error: playerEnum or direction is null");
            return false;
		}
		
		String   key         = playerEnum.name;
		
		switch(playerEnum) {
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
	}
		
		if (oldPosition.row == -1 || oldPosition.col == -1) { // if first move
			addPlayer(playerEnum);
			return true;
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
	public boolean movePlayer(PlayerEnum playerEnum, RoomEnum roomEnum) {
		if (playerEnum == null || roomEnum == null) {
            logger.error("Error: playerEnum or roomEnum is null");
            return false;
		}
		
		Position oldPosition = null             ;
		Position newPosition = new Position(roomEnum.position.row, roomEnum.position.col);
		String   key         = playerEnum.name  ;
		
		switch(playerEnum) {
		case ProfessorPlum:{oldPosition = plum   ; break;}
		case MissScarlet  :{oldPosition = scarlet; break;}
		case ColMustard   :{oldPosition = mustard; break;}
		case MrsPeacock   :{oldPosition = peacock; break;}
		case MrGreen      :{oldPosition = green  ; break;}
		case MrsWhite     :{oldPosition = white  ; break;}
		}
		
		if (oldPosition.row == -1 || oldPosition.col == -1) {
			addPlayer(playerEnum);
			return true;
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
		String toString = "";
//		String toString = "Map [gameId=" + gameId + ", grid=" + Arrays.toString(grid) + ", plum=" + plum + ", scarlet=" + scarlet
//				+ ", mustard=" + mustard + ", peacock=" + peacock + ", green=" + green + ", white=" + white + "]";
//		toString += "\n";
		for (int row = 0; row < ROW_SIZE; ++row) {
			for (int col = 0; col < COL_SIZE; ++col) {
				toString += String.format("%-50s", grid[row][col].toString());  ;
			}
			toString += "\n";
		}
		return toString;
	}
	
	public RoomEnum getRoomEnum(PlayerEnum playerEnum) {
		Position position;
		switch(playerEnum) {
		case ProfessorPlum: position = plum   ; break;
		case MissScarlet  : position = scarlet; break;
		case ColMustard   : position = mustard; break;
		case MrsPeacock   : position = peacock; break;
		case MrGreen      : position = green  ; break;
		case MrsWhite     : position = white  ; break;
		default: {
            logger.error("Error processing playerEnum");                     
			return null;
		}
		}
		
		for (RoomEnum roomEnum : roomEnums) {
			if (roomEnum.position.equals(position)) {
				return roomEnum;
			}
		}
		
		return null;
	}
	
}
