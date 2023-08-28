package com.sebastian.unobackend.gameplayer.dto;

import com.sebastian.unobackend.gameplayer.GamePlayerCard;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GamePlayerCardDTOMapper implements Function<GamePlayerCard, GamePlayerCardDTO> {
   @Override
   public GamePlayerCardDTO apply(GamePlayerCard gamePlayerCard) {
      return new GamePlayerCardDTO(
           gamePlayerCard.getCard(),
           gamePlayerCard.getReceivedAt()
      );
   }

}
