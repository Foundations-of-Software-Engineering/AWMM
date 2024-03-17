package com.awmm.messageserver.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.awmm.messageserver.jdbc.PlayerJdbcRepository;
import com.awmm.messageserver.jpa.PlayerJpaRepository;

@Component
public class PlayerCommandLineRunner implements CommandLineRunner {

//	@Autowired 
//	private PlayerJdbcRepository repository;

	@Autowired
	private PlayerJpaRepository repository;
	
	@Override
	public void run(String... args) throws Exception {
		repository.insert(new Player(1l, 1l, "Miss Scarlet", "Bathroom"));
		repository.insert(new Player(2l, 1l, "Colonel Mustard", "Study"));
		repository.insert(new Player(3l, 1l, "Chef White", "Dining Room"));
		repository.insert(new Player(4l, 1l, "Reverend Green", "Game Room"));
		
		System.out.println(repository.findById(2l));
		System.out.println(repository.findById(3l));
		
		System.out.println("Hello from Player Command Line Runner");
	}
	
	
	
}
