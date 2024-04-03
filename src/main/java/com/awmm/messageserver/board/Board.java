package com.awmm.messageserver.board;

import java.util.ArrayList;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awmm.messageserver.deck.Deck;
import com.awmm.messageserver.player.Player;

public class Board {
	
	public static final int ROW_SIZE = 5;
	public static final int COL_SIZE = 5;
	
	public final static String ProfessorPlumName = "Professor Plum";
	public final static String   MissScarletName =  "Miss Scarlet" ;
	public final static String    ColMustardName =  "Col. Mustard" ;
	public final static String    MrsPeacockName =  "Mrs. Peacock" ;
	public final static String       MrGreenName =  "Mrs. Green"   ;
	public final static String      MrsWhiteName =  "Mrs. White"   ;
	
	public final static String        RopeName = "Rope"       ;
	public final static String    LeadPipeName = "Lead Pipe"  ;
	public final static String       KnifeName = "Knife"      ;
	public final static String      WrenchName = "Wrench"     ;
	public final static String CandlestickName = "Candlestick";
	public final static String    RevolverName = "Revolver"   ;
	
	public final static String        StudyName =   "Study"        ;
	public final static String         HallName =   "Hall"         ;
	public final static String       LoungeName =   "Lounge"       ;
	public final static String      LibraryName =   "Library"      ;
	public final static String BilliardRoomName =   "Billiard Room";
	public final static String   DiningRoomName =   "Dining Room"  ;
	public final static String ConservatoryName =   "Conservatory" ;
	public final static String     BallroomName =   "Ballroom"     ;
	public final static String      KitchenName =   "Kitchen"      ;
	
    public static final String Up    = "UP"   ;
    public static final String Down  = "DOWN" ;
    public static final String Right = "RIGHT";
    public static final String Left  = "LEFT" ;
    public static final String Diagonal = "DIAGONAL";
	
    private String gameId;
	private boolean started;
	
	private ArrayList<Player> players;

	private class BoardPlayer {
		private Player player;
		private Position position;
		private BoardPlayer(int id, String gameID, String name) {
			this.player = new Player(id, gameID, name);
			this.position = new Position(-1, -1);
		}
	}

	BoardPlayer professorPlum;
	BoardPlayer missScarlet  ;
	BoardPlayer colMustard   ;
	BoardPlayer mrsPeacock   ;
	BoardPlayer mrGreen      ;
	BoardPlayer mrsWhite     ;
	
	public enum RoomEnum {
		Study       (0,0,        StudyName),
		Hall        (0,2,         HallName),
		Lounge      (0,4,       LoungeName),
		Library     (2,0,      LibraryName),
		BilliardRoom(2,2, BilliardRoomName),
		DiningRoom  (2,4,   DiningRoomName),
		Conservatory(4,0, ConservatoryName),
		Ballroom    (4,2,     BallroomName), 
		Kitchen     (4,3,      KitchenName);
		
		private final Position position;
		private final String name;
		
		RoomEnum(int row, int col, String name) {
			this.position = new Position(row, col);
			this.name = name;
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
		Position(Position position) {
			this.row = position.row;
			this.col = position.col;
		}
		private int row;
		private int col;
	}
	
	private Location[][] grid = new Location[ROW_SIZE][COL_SIZE];

	private String suspectSolution;
	private String weaponSolution ;
	private String roomSolution   ;
	
	private final Logger logger = LoggerFactory.getLogger(Board.class);

	public Board(String gameId) {
		super();
		
		professorPlum = new BoardPlayer(0, gameId, ProfessorPlumName);
		missScarlet   = new BoardPlayer(1, gameId,   MissScarletName);
		colMustard    = new BoardPlayer(2, gameId,    ColMustardName);
		mrsPeacock    = new BoardPlayer(3, gameId,    MrsPeacockName);
		mrGreen       = new BoardPlayer(4, gameId,       MrGreenName);
		mrsWhite      = new BoardPlayer(5, gameId,      MrsWhiteName);
		
		this.started = false;
		this.gameId = gameId;
		this.players = new ArrayList<Player>();
		
		//Rooms
		for (RoomEnum roomEnum : RoomEnum.values()) {
			grid[roomEnum.position.row][roomEnum.position.col] = new Room(roomEnum.name); 
		}
		
		//Hallways
		for (int i = 0; i < ROW_SIZE; ++i) {
			for (int j = 0; j < COL_SIZE; ++j) {
				if (grid[i][j] == null) {					
					grid[i][j] = new Hallway();
				}
			}
		}
		
		suspectSolution = null;
		weaponSolution  = null;
		roomSolution    = null;
	}
	
	private void addPlayer(BoardPlayer boardPlayer) {
		if (started) return;
		Player player = boardPlayer.player;
		Position startingPosition;
		switch(player.getName()) {
			case ProfessorPlumName: 
			{
				startingPosition = new Position(1, 0);
				break;
			}
			case MissScarletName  : 
			{
				startingPosition = new Position(0, 3);
				break;
			}
			case ColMustardName   : 
			{
				startingPosition = new Position(1, 4);
				break;
			}
			case MrsPeacockName   : 
			{
				startingPosition = new Position(3, 0);
				break;
			}
			case MrGreenName      : 
			{
				startingPosition = new Position(4, 1);
				break; 
			}
			case MrsWhiteName     : 
			{
				startingPosition = new Position(4, 3);
				break;
			}
			default: {return;}	
		}
		boardPlayer.position = startingPosition;
		grid[boardPlayer.position.row][boardPlayer.position.col].setPlayer(player);
		players.add(player);
	}
		
	public boolean movePlayer(String playerName, String destination) {
		BoardPlayer boardPlayer = getBoardPlayerFromName(playerName);
		
		if (boardPlayer == null || destination == null) {
			logger.error("Error: playerName or direction is null");
			return false;
		}
		
		if (boardPlayer.position.row == -1 || boardPlayer.position.col == -1) { // if it's player's first move
			addPlayer(boardPlayer);
			return true;
		}
		
		Position oldPosition = boardPlayer.position;
		String   key         = boardPlayer.player.getName();
		Position newPosition = new Position(oldPosition);
		
		switch (destination) {
			case Up:
			{
				if (oldPosition.row-1 >= 0) {
					--newPosition.row;
				}
				break;
			}
			case Down:
			{
				if (oldPosition.row+1 < ROW_SIZE) {
					++newPosition.row;
				}
				break;
			}
			case Left:
			{
				if (oldPosition.col-1 >= 0) {
					--newPosition.col;
				}
				break;
			}
			case Right:
			{
				if (oldPosition.col+1 < COL_SIZE) {
					++newPosition.col;
				}
				break;
			}
			case Diagonal:
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
			case        StudyName: { newPosition = new Position(RoomEnum.Study       .position); break; }
			case         HallName: { newPosition = new Position(RoomEnum.Hall        .position); break; }
			case       LoungeName: { newPosition = new Position(RoomEnum.Lounge      .position); break; }
			case      LibraryName: { newPosition = new Position(RoomEnum.Library     .position); break; }
			case BilliardRoomName: { newPosition = new Position(RoomEnum.BilliardRoom.position); break; }
			case   DiningRoomName: { newPosition = new Position(RoomEnum.DiningRoom  .position); break; }
			case ConservatoryName: { newPosition = new Position(RoomEnum.Conservatory.position); break; }
			case     BallroomName: { newPosition = new Position(RoomEnum.Ballroom    .position); break; }
			case      KitchenName: { newPosition = new Position(RoomEnum.Kitchen     .position); break; }
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
		String toString = "Game Answers: " + suspectSolution + ", " + weaponSolution + ", " + roomSolution + "\n";
		for (int row = 0; row < ROW_SIZE; ++row) {
			for (int col = 0; col < COL_SIZE; ++col) {
				toString += String.format("%-50s", grid[row][col].toString());  ;
			}
			toString += "\n";
		}
		return toString;
	}
	

	public void start() {
		started = true;
		String[] winningCards = Deck.dealCards(players);
		suspectSolution = winningCards[0];
		weaponSolution = winningCards[1];
		roomSolution = winningCards[2];
	}
	
	private BoardPlayer getBoardPlayerFromName(String playerName) {
		switch(playerName) {
		case ProfessorPlumName: return professorPlum;
		case   MissScarletName: return missScarlet  ;  
		case    ColMustardName: return colMustard   ;   
		case    MrsPeacockName: return mrsPeacock   ;   
		case       MrGreenName: return mrGreen      ;      
		case      MrsWhiteName: return mrsWhite     ;     
		default: return null;
		}
	}
	
	private RoomEnum getRoomEnumFromName(String roomName) {
		switch(roomName) {
	    case        StudyName: return RoomEnum.Study       ;
	    case         HallName: return RoomEnum.Hall        ;
	    case       LoungeName: return RoomEnum.Lounge      ;
	    case      LibraryName: return RoomEnum.Library     ;
	    case BilliardRoomName: return RoomEnum.BilliardRoom;
	    case   DiningRoomName: return RoomEnum.DiningRoom  ;
	    case ConservatoryName: return RoomEnum.Conservatory;
	    case     BallroomName: return RoomEnum.Ballroom    ;
	    case      KitchenName: return RoomEnum.Kitchen     ;
		default: return null;
		}
	}
	
	public void handleSuggest(String playerName, String suspect) {
		Location location = getLocation(getBoardPlayerFromName(playerName).position);
		if (location instanceof Room) {			
			movePlayer(suspect, ((Room) location).getName());
		}
	}
	
}
