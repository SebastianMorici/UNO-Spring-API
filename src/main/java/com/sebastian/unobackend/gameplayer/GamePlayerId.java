package com.sebastian.unobackend.gameplayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class GamePlayerId implements Serializable {

   @Column(name = "game_id")
   private Long gameId;
   @Column(name = "player_id")
   private Long playerId;

   public GamePlayerId() {
   }

   public GamePlayerId(Long gameId, Long playerId) {
      this.gameId = gameId;
      this.playerId = playerId;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      GamePlayerId that = (GamePlayerId) o;
      return Objects.equals(gameId, that.gameId) && Objects.equals(playerId, that.playerId);
   }

   @Override
   public int hashCode() {
      return Objects.hash(gameId, playerId);
   }
}
