package com.sebastian.unobackend.player;

import com.sebastian.unobackend.game.dto.GameDTO;
import com.sebastian.unobackend.player.dto.SearchGameDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface PlayerService extends UserDetailsService {
    List <Player> findAll();
    Player findById(Long id) throws PlayerNotFoundException;
    Player create(Player newPlayer);
    Player update(Long id, Player updatedPlayer) throws PlayerNotFoundException;
    void delete(Long id) throws PlayerNotFoundException;
    GameDTO searchGame(Long playerId, SearchGameDTO searchGameDTO) throws PlayerNotFoundException;
}