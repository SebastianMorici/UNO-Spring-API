package com.sebastian.unobackend.game.util;

import com.sebastian.unobackend.gameplayer.GamePlayer;
import com.sebastian.unobackend.card.Card;

import java.util.List;

public class GameUtil {
   public static Card getLastCard(List<Card> cards) {
      if (cards.isEmpty()) return null;

      return cards.get(cards.size() - 1);
   }

   public static Long switchTurn(GamePlayer[] players, int currentTurnIndex, boolean isReverse, boolean isSkip) {
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
