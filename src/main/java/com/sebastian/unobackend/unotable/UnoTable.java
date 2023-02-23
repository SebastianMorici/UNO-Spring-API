package com.sebastian.unobackend.unotable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.player.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class UnoTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private boolean isFull = false;
    private Long turn;
    private Long winner;
    private Card.Color currentColor;
    @Transient
    private boolean reverse = false;
    @Transient
    @JsonIgnore
    private List<Card> deck;
    @Transient
    private List<Card> playedCards;

    public UnoTable(){
        this.deck = new ArrayList<>();
        this.playedCards = new ArrayList<>();
    }

    public void shuffleDeck() {
        Collections.shuffle(this.deck);
    }

    public void deal (int amount, List<Card> receiver) {
        for (int i = 0; i < amount; i++) {
            if (this.deck.isEmpty()) break;

            Card card = this.deck.remove(0);
            receiver.add(card);
        }
    }

    // Associations
    @ManyToOne
    @JoinColumn(name = "player_one_id")
    private Player playerOne;

    @ManyToOne
    @JoinColumn(name = "player_two_id")
    private Player playerTwo;

    @ManyToOne
    @JoinColumn(name = "player_three_id")
    private Player playerThree;


}
