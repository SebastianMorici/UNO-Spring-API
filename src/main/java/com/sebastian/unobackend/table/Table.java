package com.sebastian.unobackend.table;

import com.sebastian.unobackend.player.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Table implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private boolean isFull = false;
    private Long turn;
    private Long winner;
    @Transient
    private Deck deck;
    @Transient
    private List<Card> playerOneCards, playerTwoCards, playerThreeCards, playedCards;

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
