package com.awmm.messageserver.cards;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.awmm.messageserver.board.Board;

import jakarta.transaction.Transactional;

//@Component
public class CardsCommandLineRunner implements CommandLineRunner{

//	@Autowired
	private CardsController cardsController;
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("CardsCommandLineRunner START");
		System.out.println("##################################################################");
		ArrayList<String> players = new ArrayList<>();
		players.add(Board.ProfessorPlumName);
		players.add(Board.ColMustardName);
		players.add(Board.MrsWhiteName);
		cardsController.dealCards("101", players);
		System.out.println(cardsController.getCardsRepository().getReferenceById("101"));
		
		System.out.println("##################################################################");
		System.out.println("CardsCommandLineRunner END");

	}
}
