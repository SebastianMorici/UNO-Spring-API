package com.sebastian.unobackend.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CardDataLoader implements ApplicationRunner {

    private final CardRepository cardRepository;

    @Autowired
    public CardDataLoader(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (cardRepository.count() != 0) return;

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
                    cardRepository.save(new Card(color, value));
                }
            }
        }

        // Creates "0" cards. One of each
        for (Card.Color color : colorsWithoutBlack) {
            cardRepository.save(new Card(color, Card.Value.ZERO));
        }

        // Creates wildcards. Four of each
        for (int times = 0; times < 4; times++) {
            cardRepository.save(new Card(Card.Color.BLACK, Card.Value.WILD));
            cardRepository.save(new Card(Card.Color.BLACK, Card.Value.WILD_DRAW_FOUR));
        }
    }
}
