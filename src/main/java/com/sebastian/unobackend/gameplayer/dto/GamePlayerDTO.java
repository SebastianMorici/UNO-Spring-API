package com.sebastian.unobackend.gameplayer.dto;

import com.sebastian.unobackend.card.Card;

import java.util.List;

public record GamePlayerDTO(Long playerId, List<Card> playerDeck) {
}
