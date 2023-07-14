package com.sebastian.unobackend.player;


public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(Long id) {
        super("Player with id: " + id + " not found");
    }

    public PlayerNotFoundException(String username) {
        super("Player with username: '" + username + "' not found");
    }
}
