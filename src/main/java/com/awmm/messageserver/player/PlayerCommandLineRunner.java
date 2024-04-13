package com.awmm.messageserver.player;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.awmm.messageserver.messages.ExampleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.awmm.messageserver.ClientController;
import com.awmm.messageserver.messages.Message;
import com.awmm.messageserver.board.Board;
import com.awmm.messageserver.cards.Cards;
import com.awmm.messageserver.cards.CardsRepository;
import com.awmm.messageserver.positions.Positions;
import com.awmm.messageserver.positions.PositionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.transaction.Transactional;


//@Component

public class PlayerCommandLineRunner implements CommandLineRunner {

//	@Autowired
//	private PlayerJdbcRepository repository;

//	@Autowired
//	private PlayerJpaRepository repository;

	@Autowired
	private CardsRepository cardsRepository;

	@Autowired
	private PositionsRepository positionsRepository;

    private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		System.out.println("Hello from Player Command Line Runner");

		class TestWebSocketSession implements WebSocketSession {

			String gameID;

			public String getGameID() {
				return gameID;
			}

			@Override
			public String getId() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getUri() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public HttpHeaders getHandshakeHeaders() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<String, Object> getAttributes() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Principal getPrincipal() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InetSocketAddress getLocalAddress() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InetSocketAddress getRemoteAddress() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getAcceptedProtocol() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setTextMessageSizeLimit(int messageSizeLimit) {
				// TODO Auto-generated method stub

			}

			@Override
			public int getTextMessageSizeLimit() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void setBinaryMessageSizeLimit(int messageSizeLimit) {
				// TODO Auto-generated method stub

			}

			@Override
			public int getBinaryMessageSizeLimit() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public List<WebSocketExtension> getExtensions() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void sendMessage(WebSocketMessage<?> message) throws IOException {
				// TODO Auto-generated method stub
				String jsonText = (String) message.getPayload();
				System.out.println("jsonText = " + jsonText);
				ExampleMessage msg = mapper.readValue(jsonText, ExampleMessage.class);
				gameID = msg.GAMEID();
			}

			@Override
			public boolean isOpen() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public void close(CloseStatus status) throws IOException {
				// TODO Auto-generated method stub

			}

		}


		TestWebSocketSession session = new TestWebSocketSession();
		ClientController clientController = new ClientController();

		// Test LOGIN
		Message newUserMessage = new ExampleMessage(null, 0, "LOGIN", null, null, null, "example");
		String jsonMessage = clientController.convertToJson(newUserMessage);
		TextMessage textMessage = new TextMessage(jsonMessage);
		clientController.handleTextMessage(session, textMessage);
		String gameID = session.getGameID();
		newUserMessage = new ExampleMessage(gameID, 1, "LOGIN", null, null, null, "example");
		textMessage = new TextMessage(clientController.convertToJson(newUserMessage));
		clientController.handleTextMessage(session, textMessage);
		System.out.println("gameState for gameID " + gameID + " is\n" + clientController.getGameState(gameID));

		// Test MOVE
		Message moveMessage = new ExampleMessage(gameID, 0, "MOVE", "RIGHT", null, null, "example");
		textMessage = new TextMessage(clientController.convertToJson(moveMessage));
		clientController.handleTextMessage(session, textMessage);
		moveMessage = new ExampleMessage(gameID, 1, "MOVE", "DOWN", null, null, "example");
		textMessage = new TextMessage(clientController.convertToJson(moveMessage));
		clientController.handleTextMessage(session, textMessage);
		System.out.println("gameState for gameID " + gameID + " is\n" + clientController.getGameState(gameID));

		// Test Cards Database
		System.out.println("Test Cards Database");
		System.out.println("Game ID = " + gameID);
		cardsRepository.save(new Cards(
				gameID, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName,
				Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName,
				Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName,
				Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName, Board.ProfessorPlumName));
		Cards cards = cardsRepository.getReferenceById(gameID);
		System.out.println(cards);
		if (cards != null) {
//			clientController.getGameController().setCards(cards);
		}
		System.out.println(clientController.getGameState(gameID));

		// Test Positions Database
		System.out.println("Test Positions Database");
		System.out.println("Game ID = " + gameID);


		positionsRepository.insert(new Positions(gameID,
				2, 2, // plum
				1, 1, // scarlet
				3, 3, // mustard
				4, 4, // peacock
				3, 1, // green
				1, 3 // white
				));
		Positions positions = positionsRepository.findById(gameID);
		System.out.println(positions);
		positions.setGreenCol(100);
		System.out.print(positions);
		positions = positionsRepository.findById(gameID);
		System.out.print(positions);

		clientController.getGameController().setPositions(positions);
		System.out.println(clientController.getGameState(gameID));


		// Test START
		Message startMessage = new ExampleMessage(gameID, 0, "START", null, null, null, "example");
		textMessage = new TextMessage(clientController.convertToJson(startMessage));
		clientController.handleTextMessage(session, textMessage);
		System.out.println("gameState for gameID " + gameID + " is\n" + clientController.getGameState(gameID));

		/**
		 * Create a new Board and move every player.
		 * Because it is the first time each player is moving, they will move to their
		 * respective starting positions
		 */
		Board map = new Board("0");
		map.movePlayer(Board. ProfessorPlumName, Board. Down);
		map.movePlayer(Board.   MissScarletName, Board. Down);
		map.movePlayer(Board.    ColMustardName, Board. Down);
		map.movePlayer(Board.    MrsPeacockName, Board. Down);
		map.movePlayer(Board.       MrGreenName, Board. Down);
		map.movePlayer(Board.      MrsWhiteName, Board. Down);

		System.out.println(map);

		map.movePlayer(Board.ProfessorPlumName, Board . Up      );
		map.movePlayer(Board.  MissScarletName, Board . Right   );
		map.movePlayer(Board.   ColMustardName, Board . Down    );
		map.movePlayer(Board.   MrsPeacockName, Board . Down    );
		map.movePlayer(Board.   MrsPeacockName, Board . Diagonal);
		map.movePlayer(Board.      MrGreenName, Board . Left    );
		map.movePlayer(Board.     MrsWhiteName, Board.BilliardRoomName);

		System.out.println(map);





//		repository.insert(new Player(1l, 1l, "Miss Scarlet", new Room()));
//		repository.insert(new Player(2l, 1l, "Colonel Mustard", "Study"));
//		repository.insert(new Player(3l, 1l, "Chef White", "Dining Room"));
//		repository.insert(new Player(4l, 1l, "Reverend Green", "Game Room"));

//		System.out.println(repository.findById(2l));
//		System.out.println(repository.findById(3l));





//		final int port = 8888;
//		final int backlog = 4;
//		final InetAddress bindAddress = InetAddress.getByName("127.0.0.1");
//
//		try (ServerSocket serverSocket = new ServerSocket(port, backlog, bindAddress)){
//			System.out.println("Server is listening on port: " + port);
//
//			while (true){
//				Socket socket = serverSocket.accept();
//
//				System.out.println("Client connected");
//				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				long searchTerm = Long.parseLong(reader.readLine());
//
//				String response = repository.findById(searchTerm).toString();
//
//				System.out.println("Message returned: " + response);
//				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
//				writer.println(response);
//				socket.close();
//			}
//		}
	}



}