package com.sebastian.unobackend.game;

public class GameNotInitializedException extends RuntimeException {

    public GameNotInitializedException(Long id) {
        super("Game with id: " + id + " has not been initialized");
    }
}
