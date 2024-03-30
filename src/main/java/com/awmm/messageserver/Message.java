package com.awmm.messageserver;

import org.springframework.boot.context.properties.bind.DefaultValue;

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
public record Message (@DefaultValue("0") String GAMEID,
                       @DefaultValue("0") Integer USERID,
                       @DefaultValue("none") String action,
                       @DefaultValue("none") String location,
                       @DefaultValue("none") String weapon,
                       @DefaultValue("none") String suspect){
}
