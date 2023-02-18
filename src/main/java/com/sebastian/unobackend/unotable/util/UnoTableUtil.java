package com.sebastian.unobackend.unotable.util;

import com.sebastian.unobackend.unotable.Card;

import java.util.List;

public class UnoTableUtil {
    public static Card getLastCard(List<Card> cards) {
        if (cards.isEmpty()) return null;

        return cards.get(cards.size() - 1);
    }
}
