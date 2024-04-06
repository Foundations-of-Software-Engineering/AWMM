package com.awmm.messageserver.board;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awmm.messageserver.cards.Cards;
import com.awmm.messageserver.cards.CardsController;
import com.awmm.messageserver.deck.Deck;
import com.awmm.messageserver.player.Player;

/**
 * Represents the game board for a Clue-like mystery game.
 */
public class Board {
	
	public static final int ROW_SIZE = 5;
	public static final int COL_SIZE = 5;

	// Constants for character names
	public final static String ProfessorPlumName = "Professor Plum";
	public final static String   MissScarletName =  "Miss Scarlet" ;
	public final static String    ColMustardName =  "Col. Mustard" ;
	public final static String    MrsPeacockName =  "Mrs. Peacock" ;
	public final static String       MrGreenName =  "Mrs. Green"   ;
	public final static String      MrsWhiteName =  "Mrs. White"   ;

	// Constants for weapon names
	public final static String        RopeName = "Rope"       ;
	public final static String    LeadPipeName = "Lead Pipe"  ;
	public final static String       KnifeName = "Knife"      ;
	public final static String      WrenchName = "Wrench"     ;
	public final static String CandlestickName = "Candlestick";
	public final static String    RevolverName = "Revolver"   ;

	// Constants for room names
	public final static String        StudyName =   "Study"        ;
	public final static String         HallName =   "Hall"         ;
	public final static String       LoungeName =   "Lounge"       ;
	public final static String      LibraryName =   "Library"      ;
	public final static String BilliardRoomName =   "Billiard Room";
	public final static String   DiningRoomName =   "Dining Room"  ;
	public final static String ConservatoryName =   "Conservatory" ;
	public final static String     BallroomName =   "Ballroom"     ;
	public final static String      KitchenName =   "Kitchen"      ;

	// Constants for directions
    public static final String Up    = "UP"   ;
    public static final String Down  = "DOWN" ;
    public static final String Right = "RIGHT";
    public static final String Left  = "LEFT" ;
    public static final String Diagonal = "DIAGONAL";
	
    private String gameId;
	private boolean started;
	private boolean inSuggestion;
	
	private ArrayList<Player> players;
	
	private final CardsController cardsController;

	/**
	 * Inner class representing a player on the board.
	 */
	private class BoardPlayer {
		private Player player;
		private Position position;
		private BoardPlayer(int id, String gameID, String name) {
			this.player = new Player(id, gameID, name);
			this.position = new Position(-1, -1);
		}
	}

	// BoardPlayer objects representing characters in the game
	BoardPlayer professorPlum;
	BoardPlayer missScarlet  ;
	BoardPlayer colMustard   ;
	BoardPlayer mrsPeacock   ;
	BoardPlayer mrGreen      ;
	BoardPlayer mrsWhite     ;

	// Enum representing different rooms on the board
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

		/**
		 * Constructs a RoomEnum object with the given parameters.
		 * @param row The row index of the room on the board grid.
		 * @param col The column index of the room on the board grid.
		 * @param name The name of the room.
		 */
		RoomEnum(int row, int col, String name) {
			this.position = new Position(row, col);
			this.name = name;
		}
	}

	// Array of RoomEnum objects representing rooms on the board
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

	/**
	 * Inner class representing a position on the board grid.
	 */
	static public class Position {
		/**
		 * Constructs a Position object with default values (-1, -1).
		 */
		Position() {
			row = -1;
			col = -1;
		}

		/**
		 * Returns the hash code value for this Position object.
		 * The hash code is computed based on the values of the 'row' and 'col' attributes.
		 *
		 * @return The hash code value for this object.
		 */
		@Override
		public int hashCode() {
			return Objects.hash(col, row);
		}

		/**
		 * Indicates whether some other object is "equal to" this one.
		 * Two Position objects are considered equal if they have the same 'row' and 'col' values.
		 *
		 * @param obj The reference object with which to compare.
		 * @return true if this object is the same as the obj argument; false otherwise.
		 */
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

		/**
		 * Constructs a Position object with the given row and column indices.
		 * @param row The row index.
		 * @param col The column index.
		 */
		Position(int row, int col) {
			this.row = row;
			this.col = col;
		}

		/**
		 * Constructs a Position object by copying another Position object.
		 * @param position The Position object to copy.
		 */
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

	/**
	 * Constructs a Board object with the given game ID.
	 * Initializes the game board, players, and cards.
	 * @param gameId The ID of the game.
	 */
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
		this.cardsController = new CardsController();
		
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

	/**
	 * Adds a player to the board at a specified starting position if the game hasn't started yet.
	 *
	 * @param boardPlayer The BoardPlayer object representing the player to be added.
	 */
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

	/**
	 * Moves a player to a specified destination on the board.
	 *
	 * @param playerName The name of the player to move.
	 * @param destination The direction or room name to move the player to.
	 * @return true if the player is successfully moved, false otherwise.
	 */
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

	/**
	 * Retrieves the location object at the specified position on the board grid.
	 *
	 * @param position The position for which to retrieve the location object.
	 * @return The location object at the specified position, or null if the position is out of bounds.
	 */
	private Location getLocation(Position position) {
		if (position.row >= 0 && position.row < ROW_SIZE && position.col >= 0 && position.col < COL_SIZE) {			
			return grid[position.row][position.col];
		}
		return null;
	}

	/**
	 * Moves a player to a new position on the board grid.
	 *
	 * @param key The key representing the player to move.
	 * @param oldPosition The current position of the player.
	 * @param newPosition The new position to which the player is to be moved.
	 * @return true if the player is successfully moved, false otherwise.
	 */
	private boolean move(String key, Position oldPosition, Position newPosition) {
		boolean ret = false;
		if (oldPosition.row == -1 || oldPosition.col == -1) {
			BoardPlayer boardPlayer = getBoardPlayerFromName(key);
			addPlayer(boardPlayer);
			oldPosition = boardPlayer.position;
		}
		
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

	/**
	 * Returns a string representation of the board, including game ID and solution cards.
	 *
	 * @return A string representing the game ID and solution cards.
	 */
	@Override
	public String toString() {
		String toString = "Game Answers for Game ID " + gameId +": " + suspectSolution + ", " + weaponSolution + ", " + roomSolution + "\n";
		for (int row = 0; row < ROW_SIZE; ++row) {
			for (int col = 0; col < COL_SIZE; ++col) {
				toString += String.format("%-50s", grid[row][col].toString());  ;
			}
			toString += "\n";
		}
		return toString;
	}

	/**
	 * Starts the game by dealing cards to players and setting the solution cards.
	 */
	public void start() {
		started = true; // prevents people from joining
		String[] winningCards = Deck.dealCards(players);
		suspectSolution = winningCards[0];
		weaponSolution = winningCards[1];
		roomSolution = winningCards[2];
	}
	
	public void setCards(Map<String, String> map) {
		for (String owner : map.keySet()) {
			getBoardPlayerFromName(map.get(owner)).player.receiveCard(owner);
		}
	}

	/**
	 * Retrieves the BoardPlayer object associated with the given player name.
	 *
	 * @param playerName The name of the player to retrieve.
	 * @return The BoardPlayer object associated with the given player name, or null if not found.
	 */
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

	/**
	 * Retrieves the RoomEnum object associated with the given room name.
	 *
	 * @param roomName The name of the room to retrieve.
	 * @return The RoomEnum object associated with the given room name, or null if not found.
	 */
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

	/**
	 * Handles a player's suggestion by moving the suggested suspect to the player's current location if it's a room.
	 *
	 * @param playerName The name of the suggesting player.
	 * @param suspect The name of the suggested suspect.
	 */
	public boolean handleSuggest(String playerName, String suspect) {
		Location location = getLocation(getBoardPlayerFromName(playerName).position);
		if (location instanceof Room) {			
			movePlayer(suspect, ((Room) location).getName());
			return true;
		}
		return false;
	}

	/**
	 * Sets the positions of players on the board based on a map of player names to position coordinates.
	 *
	 * @param map A map containing player names as keys and arrays of row and column coordinates as values.
	 */
	public void setPositions(Map<String, Integer[]> map) {
		for (String key : map.keySet()) {
			BoardPlayer boardPlayer = getBoardPlayerFromName(key);
			if (boardPlayer != null) {
				Integer[] position = map.get(key);
				move(key, boardPlayer.position, new Position(position[0], position[1]));
			}
		}
	}

	/**
	 * Checks if the game has started.
	 *
	 * @return true if the game has started, false otherwise.
	 */
	public boolean getStarted(){
		return started;
	}
	
}
