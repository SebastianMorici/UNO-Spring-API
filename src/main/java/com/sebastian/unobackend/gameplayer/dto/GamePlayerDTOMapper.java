package com.sebastian.unobackend.gameplayer.dto;

import com.sebastian.unobackend.gameplayer.GamePlayer;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GamePlayerDTOMapper implements Function<GamePlayer, GamePlayerDTO> {
   @Override
   public GamePlayerDTO apply(GamePlayer gamePlayer) {
      return new GamePlayerDTO(
           gamePlayer.getPlayer().getId(),
           gamePlayer.getPlayerDeck()
      );
   }
}
