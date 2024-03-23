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
		
		// Players
		Player profPlum    = new Player(1l, 1l, "Professor Plum", null);
		Player missScarlet = new Player(2l, 1l, "Miss Scarlet", null);
		Player colMustard  = new Player(3l, 1l, "Col. Mustard", null);
		Player mrsPeacock  = new Player(4l, 1l, "Mrs. Peacock", null);
		Player mrGreen     = new Player(5l, 1l, "Mr. Green", null);
		Player mrsWhite    = new Player(6l, 1l, "Mrs. White", null);
		// Rooms
		Room study        = new Room("Study", null);
		Room hall         = new Room("Hall", null);
		Room lounge       = new Room("Lounge", null);
		Room library      = new Room("Library", null);
		Room billiardRoom = new Room("Billiard Room", null);
		Room diningRoom   = new Room("Dining Room", null);
		Room conservatory = new Room("Conservatory", null);
		Room ballroom     = new Room("Ballroom", null);
		Room kitchen      = new Room("Kitchen", null);
		// Hallways
		Hallway study2Hall              = new Hallway(study, hall, "Study to Hall");
		Hallway hall2Lounge             = new Hallway(hall, lounge, "Hall to Lounge");
		Hallway study2Library           = new Hallway(study, library, "Study to Library");
		Hallway hall2BilliardRoom       = new Hallway(hall, billiardRoom, "Hall to Billiard Room");
		Hallway lounge2DiningRoom       = new Hallway(lounge, diningRoom, "Lounge to Dining Room");
		Hallway library2BilliardRoom    = new Hallway(library, billiardRoom, "Library to Billiard Room");
		Hallway billiardRoom2DiningRoom = new Hallway(billiardRoom, diningRoom, "Billiard Room to Dining Room");
		Hallway library2Conservatory    = new Hallway(library, conservatory, "Library to Conservatory");
		Hallway billiardRoom2Ballroom   = new Hallway(billiardRoom, ballroom, "Billiard Room to Ballroom");
		Hallway diningRoom2Kitchen      = new Hallway(diningRoom, kitchen, "Dining Room to Kitchen");
		Hallway conservatory2Ballroom   = new Hallway(conservatory, ballroom, "Conservatory to Ballroom");
		Hallway ballroom2Kitchen        = new Hallway(ballroom, kitchen, "Ballroom to Kitchen");
		// Set location for players
		profPlum   .setLocation(study2Library);
		missScarlet.setLocation(hall2Lounge);
		colMustard .setLocation(lounge2DiningRoom);
		mrsPeacock .setLocation(library2Conservatory);
		mrGreen    .setLocation(conservatory2Ballroom);
		mrsWhite   .setLocation(ballroom2Kitchen);
		// Set hallways for rooms
		study       .setHallways(null);
		hall        .setHallways();
		lounge      .setHallways();
		library     .setHallways();
		billiardRoom.setHallways();
		diningRoom  .setHallways();
		conservatory.setHallways();
		ballroom    .setHallways();
		kitchen     .setHallways();
		
		
		
		
		
		
		
		
		
		
		repository.insert(new Player(1l, 1l, "Miss Scarlet", new Room()));
		repository.insert(new Player(2l, 1l, "Colonel Mustard", "Study"));
		repository.insert(new Player(3l, 1l, "Chef White", "Dining Room"));
		repository.insert(new Player(4l, 1l, "Reverend Green", "Game Room"));
		
		System.out.println(repository.findById(2l));
		System.out.println(repository.findById(3l));
		
		System.out.println("Hello from Player Command Line Runner");

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