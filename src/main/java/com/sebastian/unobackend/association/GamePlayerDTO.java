package com.sebastian.unobackend.association;

import com.sebastian.unobackend.card.Card;

import java.util.List;

public record GamePlayerDTO(Long playerId, List<Card> playerDeck) {
}
