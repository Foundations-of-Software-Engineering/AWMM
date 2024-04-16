package com.awmm.messageserver.chat;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatController {

	@Autowired 
	private ChatRepository repository;
	
	public String getChat(String gameID) {
		String ret = null;
		Optional<Chat> opt = repository.findById(gameID);
		
		if (opt.isPresent()) {
			ret = opt.get().getText();
		} 
		
		return ret;
	}
	
	public void setChat(String gameID, String chat) {
		Chat newChat = new Chat(gameID, chat);
		repository.save(newChat);
	}
	
	public String append(String gameID, String end) {
		String ret = null;
		Optional<Chat> opt = repository.findById(gameID);
		
		
		if (opt.isPresent()) {
			ret = opt.get().getText();
			ret += end + "<br>";
			repository.save(new Chat(gameID, ret));
		} else {
			ret = end + "<br>";
			setChat(gameID, ret);
		}
		
		return ret;
	}
	
}
