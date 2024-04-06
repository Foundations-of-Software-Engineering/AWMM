package com.awmm.messageserver.messages;

public record ExampleMessage(String GAMEID,
                             Integer USERID,
                             String action,
                             String location,
                             String weapon,
                             String suspect,
                             String type)
        implements Message {}
