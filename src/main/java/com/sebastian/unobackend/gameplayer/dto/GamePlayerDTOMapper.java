package com.sebastian.unobackend.gameplayer.dto;

import com.sebastian.unobackend.gameplayer.GamePlayer;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GamePlayerDTOMapper implements Function<GamePlayer, GamePlayerDTO> {
   private final GamePlayerCardDTOMapper gamePlayerCardDTOMapper;

   public GamePlayerDTOMapper(GamePlayerCardDTOMapper gamePlayerCardDTOMapper) {
      this.gamePlayerCardDTOMapper = gamePlayerCardDTOMapper;
   }

   @Override
   public GamePlayerDTO apply(GamePlayer gamePlayer) {
      return new GamePlayerDTO(
           gamePlayer.getPlayer().getId(),
           gamePlayer.getPlayerDeck()
                .stream()
                .map(gamePlayerCardDTOMapper)
                .collect(Collectors.toList())
      );
   }
}
