package com.awmm.messageserver.deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.awmm.messageserver.player.Player;

public class Deck {
	final private static String[] cards = {
	//	 6 Suspects
	"Professor Plum",
	"Col. Mustard",
	"Miss Scarlet",
	"Mrs. Green",
	"Mrs. White",
	"Mrs. Peacock",
	// 6 Weapons
	"Rope",
	"LeadPipe",
	"Knife",
	"Wrench",
	"Candlestick",
	"Revolver",
	// 9 Rooms
	"Study",
	"Hall",
	"Lounge",
	"Library",
	"BilliardRoom",
	"DiningRoom",
	"Conservatory",
	"Ballroom",
	"Kitchen"
	// 6 + 6 + 9 - 3 = 18
	};
	
	public Deck() {
	}
	
	private static ArrayList<String> shuffleCards(ArrayList<String> cardsList) {
        Random rand = new Random();
        int j;
        int size = cardsList.size();
        for (int i = 0; i < size; i++) {
            j = rand.nextInt(size);
            String temp = cardsList.get(i);
            cardsList.set(i, cardsList.get(j));
            cardsList.set(j, temp);
        }
        return cardsList;
    }
	 
	public static String[] dealCards(ArrayList<Player> players) {
		String winningCards[] = new String[3];
		Random rand = new Random();
		
		ArrayList<String> cardsList = new ArrayList<>(Arrays.asList(cards));
		
		winningCards[0] = cardsList.remove(rand.nextInt(6)); // suspect
		winningCards[1] = cardsList.remove(rand.nextInt(6)+5); // weapon
		winningCards[2] = cardsList.remove(rand.nextInt(9)+10); // room

		shuffleCards(cardsList);
		int size = players.size();
        
        for (int i = 0; i < cardsList.size(); i++) {
            players.get(i % size).receiveCard(cardsList.get(i));;
        }

        return winningCards;
    }
	
}
