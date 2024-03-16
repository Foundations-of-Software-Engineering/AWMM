package com.awmm.messageserver;

import org.springframework.boot.context.properties.bind.DefaultValue;

public record Message (@DefaultValue long gameID,
                       @DefaultValue long userID,
                       @DefaultValue("none") String action,
                       @DefaultValue("none") String location,
                       @DefaultValue("none") String weapon,
                       @DefaultValue("none") String suspect){
}