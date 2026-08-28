package com.pictet.adventure.exception;

import org.springframework.http.HttpStatus;

public class AdventureException extends RuntimeException {

    private final HttpStatus status;

    public AdventureException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public AdventureException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public AdventureException(String message, Throwable cause) {
        this(message, HttpStatus.BAD_REQUEST, cause);
    }

    public AdventureException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
