package com.sebastian.unobackend.gameplayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class GamePlayerCardId {
   private GamePlayerId gamePlayerId;

   @Column(name = "card_id")
   private Long cardId;

   public GamePlayerCardId() {
   }

   public GamePlayerCardId(GamePlayerId gamePlayerId, Long cardId) {
      this.gamePlayerId = gamePlayerId;
      this.cardId = cardId;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      GamePlayerCardId that = (GamePlayerCardId) o;
      return Objects.equals(gamePlayerId, that.gamePlayerId) && Objects.equals(cardId, that.cardId);
   }

   @Override
   public int hashCode() {
      return Objects.hash(gamePlayerId, cardId);
   }
}
