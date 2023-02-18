package com.sebastian.unobackend.unotable;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        List<Card.Color> colorsWithoutBlack = Arrays.stream(Card.Color.values())
                .filter(color -> color != Card.Color.BLACK)
                .toList();
        List<Card.Value> valuesWithoutWilds = Arrays.stream(Card.Value.values())
                .filter(value -> (value != Card.Value.WILD) && (value != Card.Value.WILD_DRAW_FOUR) && (value != Card.Value.ZERO))
                .toList();
        // Creates numbers cards except "0". Two of each
        for (int times = 0; times < 2; times++){
            for (Card.Color color : colorsWithoutBlack) {
                for (Card.Value value : valuesWithoutWilds) {
                    cards.add(new Card(color, value));
                }
            }
        }
        // Creates "0" cards. One of each
        for (Card.Color color : colorsWithoutBlack) {
            cards.add(new Card(color, Card.Value.ZERO));
        }
        // Creates wildcards. Four of each
        for (int times = 0; times < 4; times++) {
            cards.add(new Card(Card.Color.BLACK, Card.Value.WILD));
            cards.add(new Card(Card.Color.BLACK, Card.Value.WILD_DRAW_FOUR));
        }
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
