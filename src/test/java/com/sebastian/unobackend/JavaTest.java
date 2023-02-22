package com.sebastian.unobackend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaTest {
    private List<String> playerOneCards = new ArrayList<>();

    private List<String> playedCards = new ArrayList<>();

    public List<String> getPlayerOneCards() {
        return playerOneCards;
    }

    public void setPlayerOneCards(List<String> playerOneCards) {
        this.playerOneCards = playerOneCards;
    }

    public List<String> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(List<String> playedCards) {
        this.playedCards = playedCards;
    }

}
