package com.awmm.messageserver.board;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awmm.messageserver.cards.CardsController;
import com.awmm.messageserver.player.Player;
import com.awmm.messageserver.position.Position;
import com.awmm.messageserver.position.PositionController;

/**
 * Represents the game board for a Clue-like mystery game.
 */
public class Board {
	
	public static final int ROW_SIZE = 5;
	public static final int COL_SIZE = 5;

	public final static int ScarletUserID = 0;
	public final static int MustardUserID = 1;
	public final static int WhiteUserID   = 2;
	public final static int GreenUserID   = 3;
	public final static int PeacockUserID = 4;
	public final static int PlumUserID    = 5;
	
	// Constants for character names
	public final static String ProfessorPlumName = "Professor Plum";
	public final static String   MissScarletName =  "Miss Scarlet" ;
	public final static String    ColMustardName =  "Col. Mustard" ;
	public final static String    MrsPeacockName =  "Mrs. Peacock" ;
	public final static String       MrGreenName =  "Mrs. Green"   ;
	public final static String      MrsWhiteName =  "Mrs. White"   ;

	// Constants for weapon names
	public final static String          RopeName = "Rope"       ;
	public final static String      LeadPipeName = "Lead Pipe"  ;
	public final static String         KnifeName = "Knife"      ;
	public final static String        WrenchName = "Wrench"     ;
	public final static String   CandlestickName = "Candlestick";
	public final static String      RevolverName = "Revolver"   ;

	// Constants for room names
	public final static String        StudyName  =   "Study"        ;
	public final static String         HallName  =   "Hall"         ;
	public final static String       LoungeName  =   "Lounge"       ;
	public final static String      LibraryName  =   "Library"      ;
	public final static String BilliardRoomName  =   "Billiard Room";
	public final static String   DiningRoomName  =   "Dining Room"  ;
	public final static String ConservatoryName  =   "Conservatory" ;
	public final static String     BallroomName  =   "Ballroom"     ;
	public final static String      KitchenName  =   "Kitchen"      ;

	// Constants for directions
    public static final String Up       = "UP"      ;
    public static final String Down     = "DOWN"    ;
    public static final String Right    = "RIGHT"   ;
    public static final String Left     = "LEFT"    ;
    public static final String Diagonal = "DIAGONAL";
	
    private String gameId;
	private boolean started;
	private boolean suggested;
	
	private ArrayList<String> players;
	
	private CardsController cardsController;

	private PositionController positionController;

	private String[] winningCards;

	/**
	 * Inner class representing a player on the board.
	 */
	private class BoardPlayer {
		private Player player;
		private Position position;
		private boolean added;
		private BoardPlayer(int id, String gameID, String name) {
			this.player = new Player(id, gameID, name);
			this.position = positionController.savePosition(gameID, id, -1, -1);
			added = false;
		}
		private void setPosition(Position newPosition) {
			this.position.setCol(newPosition.getCol());
			this.position.setRow(newPosition.getRow());
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
			this.position = new Position("", -1, row, col); // we don't care about gameid or userid here
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

	
	private Location[][] grid = new Location[ROW_SIZE][COL_SIZE];

	private final Logger logger = LoggerFactory.getLogger(Board.class);

	/**
	 * Constructs a Board object with the given game ID.
	 * Initializes the game board, players, and cards.
	 * @param gameId The ID of the game.
	 */
	public Board(String gameId, PositionController positionController, CardsController cardsController) {
		super();

		this.cardsController = new CardsController();
		this.started = false;
		this.suggested = false;
		this.gameId = gameId;
		this.players = new ArrayList<>();
		this.positionController = positionController;

		missScarlet   = new BoardPlayer(ScarletUserID, gameId,   MissScarletName);
		colMustard    = new BoardPlayer(MustardUserID, gameId,    ColMustardName);
		mrsWhite      = new BoardPlayer(WhiteUserID  , gameId,      MrsWhiteName);
		mrGreen       = new BoardPlayer(GreenUserID  , gameId,       MrGreenName);
		mrsPeacock    = new BoardPlayer(PeacockUserID, gameId,    MrsPeacockName);
		professorPlum = new BoardPlayer(PlumUserID   , gameId, ProfessorPlumName);

		//Rooms
		for (RoomEnum roomEnum : RoomEnum.values()) {
			grid[roomEnum.position.getRow()][roomEnum.position.getCol()] = new Room(roomEnum.name); 
		}
		
		//Hallways
		for (int i = 0; i < ROW_SIZE; ++i) {
			for (int j = 0; j < COL_SIZE; ++j) {
				if (grid[i][j] == null) {					
					grid[i][j] = new Hallway();
				}
			}
		}
	}

	public void addPlayer(String player) {
		if (started) return;
		BoardPlayer boardPlayer = getBoardPlayerFromName(player);
		if (!boardPlayer.added) {			
			players.add(player);
			boardPlayer.added = true;
		}
	}
	
	/**
	 * Adds a player to the board at a specified starting position if the game hasn't started yet.
	 *
	 * @param boardPlayer The BoardPlayer object representing the player to be added.
	 */
	private void firstMove(BoardPlayer boardPlayer) {
		if (started) return;
		Player player = boardPlayer.player;
		Position startingPosition;
		switch(player.getName()) {
			case ProfessorPlumName: 
			{
				startingPosition = new Position(gameId, PlumUserID, 1, 0);
				break;
			}
			case MissScarletName  : 
			{
				startingPosition = new Position(gameId, ScarletUserID, 0, 3);
				break;
			}
			case ColMustardName   : 
			{
				startingPosition = new Position(gameId, MustardUserID, 1, 4);
				break;
			}
			case MrsPeacockName   : 
			{
				startingPosition = new Position(gameId, PeacockUserID, 3, 0);
				break;
			}
			case MrGreenName      : 
			{
				startingPosition = new Position(gameId, GreenUserID, 4, 1);
				break; 
			}
			case MrsWhiteName     : 
			{
				startingPosition = new Position(gameId, WhiteUserID, 4, 3);
				break;
			}
			default: {return;}	
		}
		boardPlayer.setPosition(startingPosition);
		grid[boardPlayer.position.getRow()][boardPlayer.position.getCol()].setPlayer(player);
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
		
		if (boardPlayer.position.getRow() == -1 || boardPlayer.position.getCol() == -1) { // if it's player's first move
			firstMove(boardPlayer);
			return true;
		}
		
		Position oldPosition = boardPlayer.position;
		String   key         = boardPlayer.player.getName();
//		Position newPosition = new Position(oldPosition);
		int row = oldPosition.getRow();
		int col = oldPosition.getCol();
		Position newPosition = new Position();
		
		switch (destination) {
			case Up:
			{
				if (row-1 >= 0) {
					newPosition.setRow(row-1);
				}
				break;
			}
			case Down:
			{
				if (row+1 < ROW_SIZE) {
					newPosition.setRow(row+1);
				}
				break;
			}
			case Left:
			{
				if (col-1 >= 0) {
					newPosition.setCol(col-1);
				}
				break;
			}
			case Right:
			{
				if (col+1 < COL_SIZE) {
					newPosition.setCol(col+1);
				}
				break;
			}
			case Diagonal:
			{
				if (row == 0 && col == 0) {
					newPosition.setRow(4);
					newPosition.setCol(4);
				}
				else if (row == 0 && col == 4) {
					newPosition.setRow(4);
					newPosition.setCol(0);
				}
				else if (row == 4 && col == 0) {
					newPosition.setRow(0);
					newPosition.setCol(4);
				}
				else if (row == 4 && col == 4) {
					newPosition.setRow(0);
					newPosition.setCol(0);
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
		int row = position.getRow();
		int col = position.getCol();
		if (row >= 0 && row < ROW_SIZE && col >= 0 && col < COL_SIZE) {			
			return grid[row][col];
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
		if (oldPosition.getRow() == -1 || oldPosition.getCol() == -1) {
			BoardPlayer boardPlayer = getBoardPlayerFromName(key);
			firstMove(boardPlayer);
			return true;
		}
		
		Location oldLocation = getLocation(oldPosition);
		Location newLocation = getLocation(newPosition);
		if (oldLocation != null && newLocation != null)
		{		
			Player player = oldLocation.getPlayer(key);
			if (oldLocation.removePlayer(player) && newLocation.setPlayer(player)) {
				oldPosition.setRow(newPosition.getRow());
				oldPosition.setCol(newPosition.getCol());
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
		String toString = "";
//		String toString = "Game Answers for Game ID " + gameId +": " + suspectSolution + ", " + weaponSolution + ", " + roomSolution + "\n";
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
	 * @return
	 */
	public void start() {
		started = true; // prevents people from joining
		logger.info("Dealing cards for new game.");
		cardsController.dealCards(gameId, players);
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
	public String handleSuggest(String playerName, String suspect) {
		Location location = getLocation(getBoardPlayerFromName(playerName).position);
		if (location instanceof Room) {			
			Room room = (Room) location;
			movePlayer(suspect, room.getName());
			suggested = true;
			return room.getName();
		}
		return null;
	}

	/**
	 * Sets the positions of players on the board based on a map of player names to position coordinates.
	 *
	 * @param map A map containing player names as keys and arrays of row and column coordinates as values.
	 */
//	public void setPositions(Map<String, Integer[]> map) {
//		for (String key : map.keySet()) {
//			BoardPlayer boardPlayer = getBoardPlayerFromName(key);
//			if (boardPlayer != null) {
//				Integer[] position = map.get(key);
//				move(key, boardPlayer.position, new Position(position[0], position[1]));
//			}
//		}
//	}

	/**
	 * Checks if the game has started.
	 *
	 * @return true if the game has started, false otherwise.
	 */
	public boolean getStarted(){
		return started;
	}


	public void setStarted(boolean started) {
		this.started = started;
	}

	}
