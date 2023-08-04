package com.sebastian.unobackend.game.util;

import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.gameplayer.GamePlayer;

import java.util.List;

public class GameUtil {
   public static Card getLastCard(List<Card> cards) {
      if (cards.isEmpty()) return null;

      return cards.get(cards.size() - 1);
   }

   public static Long switchTurn(GamePlayer[] players, int currentTurnIndex, boolean isReverse, boolean isSkip) {
      int newTurnIndex;

      if (isReverse) {
         newTurnIndex = (currentTurnIndex + players.length - 1) % players.length;

         if (isSkip) newTurnIndex = (newTurnIndex + players.length - 1) % players.length;
      } else {
         newTurnIndex = (currentTurnIndex + 1) % players.length;

         if (isSkip) newTurnIndex = (newTurnIndex + 1) % players.length;
      }

      return players[newTurnIndex].getPlayer().getId();
   }

   public static List<Card> getNextPlayerDeck(GamePlayer[] players, int currentTurnIndex, boolean isReverse) {
      int newTurnIndex;

      if (isReverse) {
         newTurnIndex = (currentTurnIndex + players.length - 1) % players.length;

      } else {
         newTurnIndex = (currentTurnIndex + 1) % players.length;

      }
      return players[newTurnIndex].getPlayerDeck();
   }

}
