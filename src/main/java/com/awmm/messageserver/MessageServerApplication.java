package com.awmm.messageserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for starting the Message Server application.
 * @author AWMM
 */
@SpringBootApplication
public class MessageServerApplication {

	/**
	 * The main method to start the Message Server application.
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(MessageServerApplication.class, args);
	}
}