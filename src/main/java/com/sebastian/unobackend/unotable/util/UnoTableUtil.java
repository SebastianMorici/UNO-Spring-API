package com.sebastian.unobackend.unotable.util;

import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.player.Player;

import java.util.List;

public class UnoTableUtil {
   public static Card getLastCard(List<Card> cards) {
      if (cards.isEmpty()) return null;

      return cards.get(cards.size() - 1);
   }

   public static Long switchTurn(Player[] players, int currentTurnIndex, boolean isReverse, boolean isSkip) {
      int newTurnIndex;
//      Long[] playersId = Arrays.stream(players).map(Player::getId).toArray(Long[]::new);

      if (isReverse) {
         newTurnIndex = (currentTurnIndex + players.length - 1) % players.length;

         if (isSkip) newTurnIndex = (newTurnIndex + players.length - 1) % players.length;
      } else {
         newTurnIndex = (currentTurnIndex + 1) % players.length;

         if (isSkip) newTurnIndex = (newTurnIndex + 1) % players.length;
      }

//      return playersId[newTurnIndex];
      return players[newTurnIndex].getId();
   }

   public static List<Card> getNextPlayerDeck(Player[] players, int currentTurnIndex, boolean isReverse) {
      int newTurnIndex;

      if (isReverse) {
         newTurnIndex = (currentTurnIndex + players.length - 1) % players.length;

      } else {
         newTurnIndex = (currentTurnIndex + 1) % players.length;

      }
      return players[newTurnIndex].getDeck();
   }

//   public static void main(String[] args) {
//      Player player1 = new Player();
//      player1.setId(1l);
//      Player player2 = new Player();
//      player2.setId(2l);
//      Player player3 = new Player();
//      player3.setId(3l);
//
//
//      Player[] players = {player1, player2, player3};
//
//      int currentTurnIndex = 1;
//      Long newTurn = switchTurn(players, currentTurnIndex, true, true);
//      System.out.println("Turno anterior: " + (currentTurnIndex + 1) + " -> " + newTurn);
//
//
//   }

}
