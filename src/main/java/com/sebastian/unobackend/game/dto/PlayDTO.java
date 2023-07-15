package com.sebastian.unobackend.game.dto;

import com.sebastian.unobackend.card.Card;
import jakarta.validation.constraints.NotNull;


public record PlayDTO(@NotNull Card card, Card.Color color) {

}
