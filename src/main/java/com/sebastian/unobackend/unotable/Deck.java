package com.sebastian.unobackend.unotable;

import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.card.CardService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Deck {
    private final List<Card> cards;

    private final CardService cardService;

    @Autowired
    public Deck(CardService cardService) {
        this.cardService = cardService;
        cards = cardService.findAll();
    }

    public void shuffle () {
        Collections.shuffle(this.cards);
    }

    public void deal (int amount, List<Card> receiver) {
        for (int i = 0; i < amount; i++) {
            if (this.cards.isEmpty()) break;

            Card card = this.cards.remove(0);
            receiver.add(card);
        }
    }

//    public static void main(String[] args) {
//        List<Card> playerOneCards = new ArrayList<>();
//
//        Deck deck = new Deck();
//
//        deck.shuffle();
//
//        deck.deal(1, playerOneCards);
//        System.out.println(playerOneCards);
//
//        deck.deal(1, playerOneCards);
//        System.out.println(playerOneCards);
//
//        System.out.println(deck.getCards().size());
//        System.out.println(playerOneCards);
//
//    }

}
