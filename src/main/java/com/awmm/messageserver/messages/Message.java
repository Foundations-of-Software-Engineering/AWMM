package com.awmm.messageserver.messages;

/**
 * Represents a message in the message server application.
 *
 * @param GAMEID The ID of the game associated with the message. Default value is 0.
 * @param USERID The ID of the user associated with the message. Default value is 0.
 * @param action The action passed in the message. Default value is "none".
 * @param location The location passed in the message. Default value is "none".
 * @param weapon The weapon passed in the message. Default value is "none".
 * @param suspect The suspect passed in the message. Default value is "none".
 * @author AWMM
 */
public abstract sealed interface Message permits ConfirmStartMessage, ExampleMessage, GameIdMessage {
    String GAMEID = "0";
    Integer USERID = 0;
    String action = "none";
    String location = "none";
    String weapon = "none";
    String suspect = "none";
    String type = "none";
}

