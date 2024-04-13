package com.awmm.messageserver.messages;

public record ConfirmStartMessage(Boolean started, String type) implements Message {}
