package com.awmm.messageserver.player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.awmm.messageserver.board.Hallway;
import com.awmm.messageserver.board.Location;
import com.awmm.messageserver.board.Board;
import com.awmm.messageserver.board.Room;
import com.awmm.messageserver.jpa.PlayerJpaRepository;

@Component
public class PlayerCommandLineRunner implements CommandLineRunner {

//	@Autowired 
//	private PlayerJdbcRepository repository;

	@Autowired
	private PlayerJpaRepository repository;
	
	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("Hello from Player Command Line Runner");
		
		Board map = new Board(0);
		map.addPlayer(Board.PlayerEnum.ProfessorPlum);
		map.addPlayer(Board.PlayerEnum.MissScarlet  );
		map.addPlayer(Board.PlayerEnum.ColMustard   );
		map.addPlayer(Board.PlayerEnum.MrsPeacock   );
		map.addPlayer(Board.PlayerEnum.MrGreen      );
		map.addPlayer(Board.PlayerEnum.MrsWhite     ); 	
		
		System.out.println(map);
		
		map.movePlayer(Board.PlayerEnum.ProfessorPlum, Board.Direction.UP);
		map.movePlayer(Board.PlayerEnum.MissScarlet  , Board.Direction.RIGHT);
		map.movePlayer(Board.PlayerEnum.ColMustard   , Board.Direction.DOWN);
		map.movePlayer(Board.PlayerEnum.MrsPeacock   , Board.Direction.DOWN);
		map.movePlayer(Board.PlayerEnum.MrsPeacock   , Board.Direction.DIAGONAL);
		map.movePlayer(Board.PlayerEnum.MrGreen      , Board.Direction.LEFT);
		map.movePlayer(Board.PlayerEnum.MrsWhite     , Board.RoomName.BilliardRoom);
	
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