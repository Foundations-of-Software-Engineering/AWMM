package com.awmm.messageserver.cards;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CardsController {
	public Map<String, String> getCardsMap(Cards cards) {
		HashMap<String, String> map = new HashMap<>();
		
		for (Field field : Cards.class.getDeclaredFields()) {
		    field.setAccessible(true);
		    if (field.getType().equals(String.class)){
		        try {
					map.put(field.getName(), (String) field.get(cards));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		return map;
	}
	
//	public String getOwnerOf(String card) {
//		
//	}
}
