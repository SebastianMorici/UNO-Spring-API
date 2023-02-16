package com.sebastian.unobackend.table;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class Card {
    private final Color color;
    private final Value value;

    public enum Color {
        RED,
        BLUE,
        GREEN,
        YELLOW,
        BLACK
    }

    public enum Value {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        SKIP,
        REVERSE,
        DRAW_TWO,
        WILD,
        WILD_DRAW_FOUR
    }

}


