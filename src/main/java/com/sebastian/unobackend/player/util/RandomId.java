package com.sebastian.unobackend.player.util;

import java.util.Random;

public class RandomId {
    public static Long getRandomId(Long id1, Long id2, Long id3) {
        Long[] numbers = {id1, id2, id3};
        int randomIndex = new Random().nextInt(numbers.length);
        return numbers[randomIndex];
    }

}
