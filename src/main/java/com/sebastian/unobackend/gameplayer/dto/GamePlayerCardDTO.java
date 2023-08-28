package com.sebastian.unobackend.gameplayer.dto;

import com.sebastian.unobackend.card.Card;

import java.util.Date;

public record GamePlayerCardDTO(Card card, Date receivedAt) {
}
