package com.sebastian.unobackend.game.dto;

import com.sebastian.unobackend.gameplayer.dto.GamePlayerDTOMapper;
import com.sebastian.unobackend.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GameDTOMapper implements Function<Game, GameDTO> {

   private final GamePlayerDTOMapper gamePlayerDTOMapper;

   @Autowired
   public GameDTOMapper(GamePlayerDTOMapper gamePlayerDTOMapper) {
      this.gamePlayerDTOMapper = gamePlayerDTOMapper;
   }

   @Override
   public GameDTO apply(Game game) {
      return new GameDTO(
           game.getId(),
           game.isFull(),
           game.getNumberOfPlayers(),
           game.getTurn(),
           game.getWinner(),
           game.getCurrentColor(),
           game.getCurrentValue(),
           game.getPlayers()
                .stream()
                .map(gamePlayerDTOMapper)
                .collect(Collectors.toList())
      );
   }
}
