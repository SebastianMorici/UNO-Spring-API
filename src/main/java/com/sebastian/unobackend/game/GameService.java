package com.sebastian.unobackend.game;

import com.sebastian.unobackend.player.Player;
import com.sebastian.unobackend.player.PlayerNotFoundException;

import java.util.List;

public interface GameService {
   List<Game> findAll();

   Game findById(Long id) throws GameNotFoundException;

   Game initialize(Long gameId) throws GameNotFoundException;

   Game play(Long gameId, Long playerId, PlayDTO playDto) throws GameNotInitializedException, PlayerNotFoundException;

   Game drawCard(Long gameId, Long playerId) throws GameNotInitializedException, PlayerNotFoundException;

//   Game refresh(Long id) throws GameNotFoundException;

   void delete(Long id) throws GameNotFoundException;


//   boolean changed(Long id, int[] decksSize);

//   void clearGame(Long id);
}