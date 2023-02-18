package com.sebastian.unobackend.unotable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sebastian.unobackend.player.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
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
    @Transient
    @JsonIgnore
    private Deck deck;
    @Transient
    private List<Card> playerOneCards, playerTwoCards, playerThreeCards, playedCards;

    public UnoTable(){
        this.deck = new Deck();
        this.playerOneCards = new ArrayList<>();
        this.playerTwoCards = new ArrayList<>();
        this.playerThreeCards = new ArrayList<>();
        this.playedCards = new ArrayList<>();
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
