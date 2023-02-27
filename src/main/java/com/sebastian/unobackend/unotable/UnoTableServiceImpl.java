package com.sebastian.unobackend.unotable;

import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.card.CardRepository;
import com.sebastian.unobackend.player.Player;
import com.sebastian.unobackend.player.PlayerNotFoundException;
import com.sebastian.unobackend.player.util.RandomId;
import com.sebastian.unobackend.unotable.util.UnoTableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class UnoTableServiceImpl implements UnoTableService {

   private final UnoTableRepository unoTableRepository;
   private final CardRepository cardRepository;
   private UnoTable unoTable;

   @Autowired
   public UnoTableServiceImpl(UnoTableRepository unoTableRepository, CardRepository cardRepository) {
      this.unoTableRepository = unoTableRepository;
      this.cardRepository = cardRepository;
   }

   @Override
   public UnoTable initialize(Long unoTableId) {
      // Verifies that unoTable has not been already initialized
      if (unoTable != null) return unoTable;

      unoTable = unoTableRepository
           .findById(unoTableId)
           .orElseThrow(() -> new UnoTableNotFoundException(unoTableId));
      // Set turn randomly
      Long id1 = unoTable.getPlayerOne().getId();
      Long id2 = unoTable.getPlayerTwo().getId();
      Long id3 = unoTable.getPlayerThree().getId();
      unoTable.setTurn(RandomId.getRandomId(id1, id2, id3));
      // Set deck
      unoTable.setDeck(cardRepository.findAll());
      // Shuffle deck
      unoTable.shuffleDeck();
      // Deal seven cards to each player
      unoTable.deal(7, unoTable.getPlayerOne().getDeck());
      unoTable.deal(7, unoTable.getPlayerTwo().getDeck());
      unoTable.deal(7, unoTable.getPlayerThree().getDeck());
      // Deal one card to playedCard
      do {
         unoTable.deal(1, unoTable.getPlayedCards());
      } while (UnoTableUtil.getLastCard(unoTable.getPlayedCards()).getColor() == Card.Color.BLACK);
      unoTable.setCurrentColor(UnoTableUtil.getLastCard(unoTable.getPlayedCards()).getColor());

      return unoTableRepository.save(unoTable);
   }

   @Override
   public UnoTable play(Long unoTableId, Play play) {
      // Verifies unoTable has been initialized
      if (!unoTable.getId().equals(unoTableId)) throw new UnoTableNotInitializedException(unoTableId);
      // Verifies the game on this table isn't over
      if (unoTable.getWinner() != null) return unoTable;

      Player[] players = {unoTable.getPlayerOne(), unoTable.getPlayerTwo(), unoTable.getPlayerThree()};
      Player player = Arrays.stream(players)
           .filter(p -> p.getId().equals(play.getPlayerId()))
           .findFirst()
           .orElse(null);

      if (player == null) throw new PlayerNotFoundException(play.getPlayerId());

      List<Card> playerCards = player.getDeck();

      // Verifies if it's his turn
      if (!(unoTable.getTurn().equals(player.getId()))) return unoTable;
      // Verifies if he has the played card
      if (!playerCards.contains(play.getCard())) return unoTable;

      Card lastPlayedCard = UnoTableUtil.getLastCard(unoTable.getPlayedCards());
      Card playedCard = play.getCard();

      // Verifies playedCard has the same Color or Value of the lastPlayedCard
      if (!(playedCard.getColor().equals(unoTable.getCurrentColor()) ||
           playedCard.getValue().equals(lastPlayedCard.getValue()) ||
           playedCard.getColor().equals(Card.Color.BLACK))) {
         return unoTable;
      }

      // Adds the play.card to playedCard and removes it from the player's deck
      unoTable.getPlayedCards().add(playedCard);
      playerCards.remove(playedCard);
      // Sets currentColor to the lastPlayed's color
      unoTable.setCurrentColor(playedCard.getColor());
      // If the card was the last one of his/her deck, there is a winner and the game ends.
      if (playerCards.isEmpty()) {
         unoTable.setWinner(player.getId());
         unoTableRepository.save(unoTable);
         return unoTable;
      }

      int currentTurnIndex = IntStream.range(0, players.length)
           .filter(i -> unoTable.getTurn().equals(players[i].getId()))
           .findFirst()
           .orElse(-1);


      boolean isSkip = false;
      // Effects of the special cards (SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR)
      switch (playedCard.getValue()) {
         case SKIP -> {
            isSkip = true;
            break;
         }
         case REVERSE -> {
            unoTable.setReverse(!unoTable.isReverse());
            break;
         }
         case DRAW_TWO -> {
//            Long playerId = players[currentTurnIndex];
            List<Card> nextPlayerDeck = UnoTableUtil.getNextPlayerDeck(players, currentTurnIndex, unoTable.isReverse());
            unoTable.deal(2, nextPlayerDeck);
            break;
         }
         case WILD -> {
            unoTable.setCurrentColor(play.getColor());
            break;
         }
         case WILD_DRAW_FOUR -> {
            unoTable.setCurrentColor(play.getColor());
            List<Card> nextPlayerDeck = UnoTableUtil.getNextPlayerDeck(players, currentTurnIndex, unoTable.isReverse());
            unoTable.deal(4, nextPlayerDeck);
            break;
         }
      }
      // Sets new Turn based on the last played card
      Long newTurn = UnoTableUtil.switchTurn(players, currentTurnIndex, unoTable.isReverse(), isSkip);
      unoTable.setTurn(newTurn);

      return unoTable;

   }

   @Override
   public UnoTable drawCard(Long unoTableId, Long playerId) {
      // Verifies unoTable has been initialized
      if (!unoTable.getId().equals(unoTableId)) throw new UnoTableNotInitializedException(unoTableId);
      // Verifies the game on this table isn't over
      if (unoTable.getWinner() != null) return unoTable;

      Player[] players = {unoTable.getPlayerOne(), unoTable.getPlayerTwo(), unoTable.getPlayerThree()};
      Player player = Arrays.stream(players)
           .filter(p -> p.getId().equals(playerId))
           .findFirst()
           .orElse(null);

      if (player == null) throw new PlayerNotFoundException(playerId);

      // Verifies if it's his turn
      if (!(unoTable.getTurn().equals(player.getId()))) return unoTable;

      unoTable.deal(1, player.getDeck());
      // If deck has only one card left, collects all the cards from the playedCards and adds them to deck
      if (unoTable.getDeck().size() <= 1) {
         List<Card> cardsFromPlayedCards = unoTable.getPlayedCards().subList(0, unoTable.getPlayedCards().size() - 1);
         unoTable.getDeck().addAll(cardsFromPlayedCards);
         unoTable.shuffleDeck();
      }

      int currentTurnIndex = IntStream.range(0, players.length)
           .filter(i -> unoTable.getTurn().equals(players[i].getId()))
           .findFirst()
           .orElse(-1);

      Long newTurn = UnoTableUtil.switchTurn(players, currentTurnIndex, unoTable.isReverse(), false);
      unoTable.setTurn(newTurn);

      return unoTable;
   }

   @Override
   public boolean changed(Long unoTableId, int[] decksSize) {
      return (decksSize[0] != unoTable.getPlayerOne().getDeck().size()) ||
           (decksSize[1] != unoTable.getPlayerTwo().getDeck().size()) ||
           (decksSize[2] != unoTable.getPlayerThree().getDeck().size());
   }

   @Override
   public List<UnoTable> findAll() {
      return unoTableRepository.findAll();
   }

   @Override
   public UnoTable findById(Long id) {
      return unoTableRepository
           .findById(id)
           .orElseThrow(() -> new UnoTableNotFoundException(id));
   }

   @Override
   public void delete(Long id) {
      UnoTable unoTable = unoTableRepository
           .findById(id)
           .orElseThrow(() -> new UnoTableNotFoundException(id));
      unoTableRepository.delete(unoTable);
   }

   public UnoTable refresh(Long id) {
      if (unoTable != null && unoTable.getId().equals(id)) return unoTable;
      return unoTableRepository
           .findById(id)
           .orElseThrow(() -> new UnoTableNotFoundException(id));
   }

   @Override
   public void clearUnoTable(Long unoTableId) {
      if (unoTable == null) return;
      if (!unoTable.getId().equals(unoTableId)) return;
      unoTable = null;
   }

}
