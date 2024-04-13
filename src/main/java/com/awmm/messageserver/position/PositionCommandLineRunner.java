package com.awmm.messageserver.position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
public class PositionCommandLineRunner implements CommandLineRunner {

	@Autowired
	private PositionController positionController;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("PositionCommandLineRunner START");
		System.out.println("##################################################################");
		
		Position position1 = positionController.savePosition(new Position("101", 0, 0, 0));
		System.out.println(position1);
		position1.setCol(999);
		System.out.println(positionController.getPosition("101", 0));
		
		
		System.out.println("##################################################################");
		System.out.println("PositionCommandLineRunner END");
	}
	
	
	
}
