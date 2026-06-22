package com.arpit.mythicgates.exception.custom;

public class BossAlreadyExistsException extends RuntimeException {
    public BossAlreadyExistsException(String message) {
        super(message);
    }
}
