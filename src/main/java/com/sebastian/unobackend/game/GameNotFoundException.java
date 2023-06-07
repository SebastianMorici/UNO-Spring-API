package com.sebastian.unobackend.game;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long id) {
        super("Game with id: " + id + " not found");
    }
}
