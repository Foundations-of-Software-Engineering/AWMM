package com.awmm.messageserver.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

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
import com.awmm.messageserver.Message;
import com.awmm.messageserver.board.Board;
import com.awmm.messageserver.jpa.PlayerJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PlayerCommandLineRunner implements CommandLineRunner {

//	@Autowired 
//	private PlayerJdbcRepository repository;

	@Autowired
	private PlayerJpaRepository repository;
	
    private final ObjectMapper mapper = new ObjectMapper();

	@Override
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
				Message msg = mapper.readValue(jsonText, Message.class);
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
		
		Message newUserMessage = new Message(null, 0, "LOGIN", null, null, null);
		String jsonMessage = clientController.convertToJson(newUserMessage);
		TextMessage textMessage = new TextMessage(jsonMessage);
		
		clientController.handleTextMessage(session, textMessage);
		
		String gameID = session.getGameID();
		
		System.out.println("gameID generated: " + gameID);
		
		System.out.println("gameState for gameID " + gameID + " is\n" + clientController.getGameState(gameID));
		
		Message moveMessage = new Message(gameID, 0, "MOVE", "RIGHT", null, null);
		
		textMessage = new TextMessage(clientController.convertToJson(moveMessage));
		
		clientController.handleTextMessage(session, textMessage);
		
		System.out.println("gameState for gameID " + gameID + " is\n" + clientController.getGameState(gameID));

		
		/**
		 * Create a new Board and move every player.
		 * Because it is the first time each player is moving, they will move to their
		 * respective starting positions
		 */
		Board map = new Board("0");
		map.movePlayer(Board.PlayerEnum.ProfessorPlum, Board.RoomEnum.Ballroom);
		map.movePlayer(Board.PlayerEnum.MissScarlet  , Board.RoomEnum.Ballroom);
		map.movePlayer(Board.PlayerEnum.ColMustard   , Board.RoomEnum.Ballroom);
		map.movePlayer(Board.PlayerEnum.MrsPeacock   , Board.RoomEnum.Ballroom);
		map.movePlayer(Board.PlayerEnum.MrGreen      , Board.RoomEnum.Ballroom);
		map.movePlayer(Board.PlayerEnum.MrsWhite     , Board.RoomEnum.Ballroom); 	
		
		System.out.println(map);
		
		map.movePlayer(Board.PlayerEnum.ProfessorPlum, Board.Direction.UP);
		map.movePlayer(Board.PlayerEnum.MissScarlet  , Board.Direction.RIGHT);
		map.movePlayer(Board.PlayerEnum.ColMustard   , Board.Direction.DOWN);
		map.movePlayer(Board.PlayerEnum.MrsPeacock   , Board.Direction.DOWN);
		map.movePlayer(Board.PlayerEnum.MrsPeacock   , Board.Direction.DIAGONAL);
		map.movePlayer(Board.PlayerEnum.MrGreen      , Board.Direction.LEFT);
		map.movePlayer(Board.PlayerEnum.MrsWhite     , Board.RoomEnum.BilliardRoom);
	
		System.out.println(map);
		
//		repository.insert(new Player(1l, 1l, "Miss Scarlet", new Room()));
//		repository.insert(new Player(2l, 1l, "Colonel Mustard", "Study"));
//		repository.insert(new Player(3l, 1l, "Chef White", "Dining Room"));
//		repository.insert(new Player(4l, 1l, "Reverend Green", "Game Room"));
		
//		System.out.println(repository.findById(2l));
//		System.out.println(repository.findById(3l));
		
		
		
		

		final int port = 8888;
		final int backlog = 4;
		final InetAddress bindAddress = InetAddress.getByName("127.0.0.1");

		try (ServerSocket serverSocket = new ServerSocket(port, backlog, bindAddress)){
			System.out.println("Server is listening on port: " + port);

			while (true){
				Socket socket = serverSocket.accept();

				System.out.println("Client connected");
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				long searchTerm = Long.parseLong(reader.readLine());

				String response = repository.findById(searchTerm).toString();

				System.out.println("Message returned: " + response);
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				writer.println(response);
				socket.close();
			}
		}
	}
	
	
	
}