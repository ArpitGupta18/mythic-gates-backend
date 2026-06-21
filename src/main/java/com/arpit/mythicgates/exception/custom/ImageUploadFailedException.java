package com.arpit.mythicgates.exception.custom;

public class ImageUploadFailedException extends RuntimeException{
    public ImageUploadFailedException(String message) {
        super(message);
    }
}
