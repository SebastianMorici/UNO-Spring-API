package com.sebastian.unobackend.table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

//    public static void main(String[] args) {
//        Deck deck = new Deck();
//
//        deck.getCards().forEach(card -> System.out.println(card.toString()));
//        deck.shuffle(deck.getCards());
//        System.out.println();
//        deck.getCards().forEach(card -> System.out.println(card.toString()));
//    }

}
