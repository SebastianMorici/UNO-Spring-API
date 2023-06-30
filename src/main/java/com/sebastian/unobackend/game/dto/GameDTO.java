package com.sebastian.unobackend.game.dto;

import com.sebastian.unobackend.association.GamePlayerDTO;
import com.sebastian.unobackend.card.Card;

import java.util.List;

public record GameDTO(
     Long gameId,
     boolean full,
     int numberOfPlayers,
     Long turn,
     Long winner,
     Card.Color currentColor,
     Card.Value currentValue,
     List<GamePlayerDTO> players
) {
}
