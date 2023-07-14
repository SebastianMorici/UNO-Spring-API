package com.sebastian.unobackend.gameplayer;

public class GamePlayerNotFoundException extends RuntimeException {
    public GamePlayerNotFoundException(Long gameId, Long playerId) {
        super("Player with id: " + playerId + " in Game with id: " + gameId + " not found");
    }
}
