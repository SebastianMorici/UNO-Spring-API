package com.sebastian.unobackend.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sebastian.unobackend.association.GamePlayer;
import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.player.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Game implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;
   private int numberOfPlayers;
   private boolean isFull = false;
   private Long turn;
   private Long winner;
   private Card.Color currentColor;
   private Card.Value currentValue;
   private boolean reverse = false;

   // Associations
//   @JsonIgnore
   @ManyToMany
   @JoinTable(name = "main_deck",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "card_id"))
   private List<Card> deck;

   @ManyToMany
   @JoinTable(name = "played_cards",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "card_id"))
   private List<Card> playedCards;

   @OneToMany(
        mappedBy = "game",
        cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
        orphanRemoval = true)
   private Set<GamePlayer> players;

   public Game() {
      this.deck = new ArrayList<>();
      this.playedCards = new ArrayList<>();
      this.players = new LinkedHashSet<>();
   }

   public void shuffleDeck() {
      Collections.shuffle(this.deck);
   }

   public void deal(int amount, List<Card> receiver) {
      for (int i = 0; i < amount; i++) {
         if (this.deck.isEmpty()) break;

         Card card = this.deck.remove(0);
         receiver.add(card);
      }
   }

   public void addPlayer(Player player) {
      GamePlayer gamePlayer = new GamePlayer(this, player);
      players.add(gamePlayer);
      player.getGames().add(gamePlayer);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Game)) return false;
      return id != null && id.equals(((Game) o).getId());
   }

   @Override
   public int hashCode() {
      return getClass().hashCode();
   }

}
