package com.arpit.mythicgates.exception.custom;

public class CharacterAlreadyExistsException extends RuntimeException {
    public CharacterAlreadyExistsException(String message) {
        super(message);
    }
}
