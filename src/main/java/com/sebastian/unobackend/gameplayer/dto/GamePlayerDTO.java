package com.sebastian.unobackend.gameplayer.dto;

import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.gameplayer.GamePlayerCard;

import java.util.List;

public record GamePlayerDTO(Long playerId, List<GamePlayerCard> playerDeck) {
}
