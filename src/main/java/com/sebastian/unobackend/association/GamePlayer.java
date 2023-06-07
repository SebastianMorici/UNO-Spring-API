package com.sebastian.unobackend.association;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.game.Game;
import com.sebastian.unobackend.player.Player;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "game_player")
public class GamePlayer implements Serializable {

   @EmbeddedId
   private GamePlayerId id;

   @JsonIgnore
   @ManyToOne(fetch = FetchType.LAZY)
   @MapsId("gameId")
   private Game game;

   @JsonIgnore
   @ManyToOne(fetch = FetchType.LAZY)
   @MapsId("playerId")
   private Player player;

   @ManyToMany
   @JoinTable(
        name = "player_deck",
        joinColumns = {
             @JoinColumn(name = "gameplayer_game_id", referencedColumnName = "game_id"),
             @JoinColumn(name = "gameplayer_player_id", referencedColumnName = "player_id")
        },
        inverseJoinColumns = @JoinColumn(name = "card_id")
   )
   private List<Card> playerDeck = new ArrayList<>();

   // Constructors
   public GamePlayer() {
   }

   public GamePlayer(Game game, Player player) {
      this.game = game;
      this.player = player;
      this.id = new GamePlayerId(game.getId(), player.getId());
   }

   // Getters, setters, equals and hashCode
   public GamePlayerId getId() {
      return id;
   }

   public void setId(GamePlayerId id) {
      this.id = id;
   }

   public Game getGame() {
      return game;
   }

   public void setGame(Game game) {
      this.game = game;
   }

   public Player getPlayer() {
      return player;
   }

   public void setPlayer(Player player) {
      this.player = player;
   }

   public List<Card> getPlayerDeck() {
      return playerDeck;
   }

   public void setPlayerDeck(List<Card> playerDeck) {
      this.playerDeck = playerDeck;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      GamePlayer that = (GamePlayer) o;
      return Objects.equals(game, that.game) && Objects.equals(player, that.player);
   }

   @Override
   public int hashCode() {
      return Objects.hash(game, player);
   }
}
