package com.pictet.adventure.exception;

public class AdventureException extends RuntimeException {

    public AdventureException(String message) {
        super(message);
    }

    public AdventureException(String message, Throwable cause) {
        super(message, cause);
    }
}
