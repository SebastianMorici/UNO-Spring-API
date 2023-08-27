package com.sebastian.unobackend.gameplayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.game.Game;
import com.sebastian.unobackend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;


@Entity
@Getter
@Setter
@Table(name = "game_player")
public class GamePlayer implements Serializable {

   @EmbeddedId
   private GamePlayerId id;

   @ManyToOne(fetch = FetchType.LAZY)
   @MapsId("gameId")
   private Game game;

   @ManyToOne(fetch = FetchType.LAZY)
   @MapsId("playerId")
   private Player player;

   private Date joinedAt;

   @OneToMany(mappedBy = "gamePlayer")
   private List<GamePlayerCard> playerDeck = new ArrayList<>();

   // Constructors
   public GamePlayer() {
   }

   public GamePlayer(Game game, Player player) {
      this.game = game;
      this.player = player;
      this.id = new GamePlayerId(game.getId(), player.getId());
      this.joinedAt = new Date();
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
