package com.awmm.messageserver.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.awmm.messageserver.board.Board;

@Component
public class CardsController {
	
	final private static String[] cardsArray = {
	//	 6 Suspects
	Board.ProfessorPlumName,
	Board.  MissScarletName,
	Board.   ColMustardName,
	Board.   MrsPeacockName,
	Board.      MrGreenName,
	Board.     MrsWhiteName,
	// 6 Weapons
	Board.       RopeName,
	Board.   LeadPipeName,
	Board.      KnifeName,
	Board.     WrenchName,
	Board.CandlestickName,
	Board.   RevolverName,
	// 9 Rooms
	Board.       StudyName,
	Board.        HallName,
	Board.      LoungeName,
	Board.     LibraryName,
	Board.BilliardRoomName,
	Board.  DiningRoomName,
	Board.ConservatoryName,
	Board.    BallroomName,
	Board.     KitchenName,
	// 6 + 6 + 9 - 3 = 18
	};

	public CardsRepository getCardsRepository() {
		return repository;
	}

	@Autowired
	private CardsRepository repository;
	
	public String getOwnerOf(String gameID, String card) {
		Optional<Cards> opt = repository.findById(gameID);
		if (opt.isPresent()) {
			return opt.get().get(card);
		}
		return "Not Found";
	}
	
	public boolean hasSuggestion(String gameID) {
		boolean ret = false;
		Optional<Cards> opt = repository.findById(gameID);
		if (opt.isPresent()) {
			Cards cards = opt.get();
			ret = cards.getSuggestedRoom() == null && cards.getSuggestedSuspect() == null && cards.getSuggestedWeapon() == null;
		}
		return ret;
	}
	
	public boolean checkSuggestion(String gameID, String weapon, String suspect, String room, String owner) {
		boolean ret = false;
		Optional<Cards> opt = repository.findById(gameID);
		if (opt.isPresent()) {
			Cards cards = opt.get();
			if (weapon != null && cards.get(weapon).equals(owner) && cards.getSuggestedWeapon().equals(weapon)) {
				ret = true;
			}
			else if (suspect != null && cards.get(suspect).equals(owner) && cards.getSuggestedSuspect().equals(suspect)) {
				ret = true;
			}
			else if (room != null && cards.get(room).equals(owner) && cards.getSuggestedRoom().equals(room)) {
				ret = true;
			}
		}
		return ret;
	}
	
	public boolean setSuggestion(String gameID, String weapon, String suspect, String room) {
		try {
			Cards cards = repository.getReferenceById(gameID);
			if (cards != null && cards.getSuggestedRoom() == null && cards.getSuggestedSuspect() == null && cards.getSuggestedWeapon() == null) {				
				cards.setSuggestedWeapon(weapon);
				cards.setSuggestedRoom(room);
				cards.setSuggestedSuspect(suspect);
				return true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public HashMap<String, ArrayList<String>> dealCards(String gameID, ArrayList<String> players) {
		Random rand = new Random();
		ArrayList<String> cardList = new ArrayList<>(Arrays.asList(cardsArray));

		HashMap<String, ArrayList<String>> map = new HashMap<>();
		
		Cards cards = new Cards();
		cards.setGameID(gameID);
		
		cardList.remove(rand.nextInt(6)); // suspect
		cardList.remove(rand.nextInt(6)+5); // weapon
		cardList.remove(rand.nextInt(9)+10); // room

		shuffleCards(cardList);
		
		int playersSize = players.size();
		int cardListSize = cardList.size();
		
		String player;
		String card;
		for (int i = 0; i < cardListSize; ++i) {
			player = players.get(i % playersSize);
			card = cardList.get(i);
			cards.set(player, card);
			if (map.containsKey(player)) {
				map.get(player).add(card);
			}
			else {
				map.put(player, new ArrayList<>());
				map.get(player).add(card);
			}
		}
		
		repository.save(cards);
		return map;
	}
	
	public void deleteCards(String gameID) {
		repository.deleteById(gameID);
	}
	
	public CardsController() {
		super();
		// TODO Auto-generated constructor stub
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
	
	
}
