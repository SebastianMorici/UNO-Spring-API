package com.sebastian.unobackend.player;

import com.sebastian.unobackend.unotable.UnoTable;

import java.util.List;

public interface PlayerService {
    List <Player> findAll();
    Player findById(Long id) throws PlayerNotFoundException;
    Player create(Player newPlayer);
    Player update(Long id, Player updatedPlayer) throws PlayerNotFoundException;
    void delete(Long id) throws PlayerNotFoundException;
    Player login(Player loginPlayer) throws PlayerNotFoundException;
    UnoTable searchGame(Long playerId);
//    Long searchGame(Long playerId);

}