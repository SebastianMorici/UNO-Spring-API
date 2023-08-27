package com.sebastian.unobackend.gameplayer;

import com.sebastian.unobackend.card.Card;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "game_player_card")
public class GamePlayerCard {

   @EmbeddedId
   private GamePlayerCardId id;

   @MapsId("gamePlayerId")
   @ManyToOne(fetch = FetchType.LAZY)
   private GamePlayer gamePlayer;

   @MapsId("cardId")
   @ManyToOne(fetch = FetchType.LAZY)
   private Card card;

   private Date receivedAt;

   public GamePlayerCard() {}

   public GamePlayerCard(GamePlayer gamePlayer, Card card) {
      this.gamePlayer = gamePlayer;
      this.card = card;
      this.id = new GamePlayerCardId(gamePlayer.getId(), card.getId());
      this.receivedAt = new Date();
   }




}
