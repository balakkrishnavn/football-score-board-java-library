package org.sport.exception;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String message) {
        super(String.format("Following game were not found: %s", message));
    }
}
