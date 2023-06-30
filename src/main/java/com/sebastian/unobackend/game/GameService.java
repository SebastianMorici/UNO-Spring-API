package com.sebastian.unobackend.game;

import com.sebastian.unobackend.game.dto.GameDTO;
import com.sebastian.unobackend.game.dto.PlayDTO;
import com.sebastian.unobackend.player.PlayerNotFoundException;

import java.util.List;

public interface GameService {
   List<GameDTO> findAll();

   GameDTO findById(Long id) throws GameNotFoundException;

   GameDTO initialize(Long gameId) throws GameNotFoundException;

   GameDTO play(Long gameId, Long playerId, PlayDTO playDto) throws GameNotInitializedException, PlayerNotFoundException;

   GameDTO drawCard(Long gameId, Long playerId) throws GameNotInitializedException, PlayerNotFoundException;

//   Game refresh(Long id) throws GameNotFoundException;

   void delete(Long id) throws GameNotFoundException;


//   boolean changed(Long id, int[] decksSize);

//   void clearGame(Long id);
}