package com.sebastian.unobackend.game;

import com.sebastian.unobackend.association.GamePlayer;
import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.card.CardRepository;
import com.sebastian.unobackend.player.Player;
import com.sebastian.unobackend.player.PlayerNotFoundException;
import com.sebastian.unobackend.game.util.GameUtil;
import com.sebastian.unobackend.player.PlayerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class GameServiceImpl implements GameService {

   private final GameRepository gameRepository;
   private final CardRepository cardRepository;
   private final PlayerRepository playerRepository;
//   private Game game;

   @Autowired
   public GameServiceImpl(GameRepository gameRepository, CardRepository cardRepository,
                          PlayerRepository playerRepository) {
      this.gameRepository = gameRepository;
      this.cardRepository = cardRepository;
      this.playerRepository = playerRepository;
   }

   @Override
   public Game initialize(Long gameId) {
      Game game = gameRepository
           .findById(gameId)
           .orElseThrow(() -> new GameNotFoundException(gameId));

      // Sets turn randomly
      ArrayList<GamePlayer> auxPlayersList = new ArrayList<>(game.getPlayers());
      Collections.shuffle(auxPlayersList);
      game.setTurn(auxPlayersList.get(0).getPlayer().getId());
      // Sets deck
      game.setDeck(cardRepository.findAll());
      // Shuffles deck
      game.shuffleDeck();
      // Deals seven cards to each player
      for(GamePlayer gamePlayer : game.getPlayers()){
         game.deal(7, gamePlayer.getPlayerDeck());
      }
      // Deals one card to playedCards
      do {
         game.deal(1, game.getPlayedCards());
      } while (GameUtil.getLastCard(game.getPlayedCards()).getColor() == Card.Color.BLACK);

      return gameRepository.save(game);
   }

   @Override
   public Game play(Long gameId, Long playerId, PlayDTO playDto) {
      Game game = gameRepository
           .findById(gameId)
           .orElseThrow(() -> new GameNotFoundException(gameId));
      // Verifies the game isn't over
      if (game.getWinner() != null) return game;

      // Verifies the player is in the game
      GamePlayer gamePlayer = game.getPlayers().stream()
           .filter(gp -> gp.getPlayer().getId().equals(playerId))
           .findFirst()
           .orElse(null);

      if (gamePlayer == null) throw new PlayerNotFoundException(playerId);

      // Verifies if it's his turn
      if (!(game.getTurn().equals(gamePlayer.getPlayer().getId()))) return game;
      // Verifies if he has the played card
      List<Card> playerCards = gamePlayer.getPlayerDeck();
      if (!playerCards.contains(playDto.card())) return game;

      Card lastPlayedCard = GameUtil.getLastCard(game.getPlayedCards());
      Card playedCard = playDto.card();

      // Verifies playedCard has the same Color or Value of the lastPlayedCard
      if (!(playedCard.getColor().equals(game.getCurrentColor()) ||
           playedCard.getValue().equals(lastPlayedCard.getValue()) ||
           playedCard.getColor().equals(Card.Color.BLACK))) {
         return game;
      }

      // Adds the playDto.card to playedCard and removes it from the player's deck
      game.getPlayedCards().add(playedCard);
      playerCards.remove(playedCard);
      // Sets currentColor to the lastPlayed's color
      game.setCurrentColor(playedCard.getColor());
      // If the card was the last one of his/her deck, there is a winner and the game ends.
      if (playerCards.isEmpty()) {
         game.setWinner(gamePlayer.getPlayer().getId());
         return gameRepository.save(game);
      }

      // Converts LinkedHashSet players to array because it's easier to work with indexes
      GamePlayer[] playersArr = game.getPlayers().toArray(GamePlayer[]::new);
      int currentTurnIndex = IntStream.range(0, playersArr.length)
           .filter(i -> game.getTurn().equals(playersArr[i].getPlayer().getId()))
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
            game.setReverse(!game.isReverse());
            break;
         }
         case DRAW_TWO -> {
//            Long playerId = players[currentTurnIndex];
            List<Card> nextPlayerDeck = GameUtil.getNextPlayerDeck(playersArr, currentTurnIndex, game.isReverse());
            game.deal(2, nextPlayerDeck);
            break;
         }
         case WILD -> {
            game.setCurrentColor(playDto.color());
            break;
         }
         case WILD_DRAW_FOUR -> {
            game.setCurrentColor(playDto.color());
            List<Card> nextPlayerDeck = GameUtil.getNextPlayerDeck(playersArr, currentTurnIndex, game.isReverse());
            game.deal(4, nextPlayerDeck);
            break;
         }
      }
      // Sets new Turn based on the last played card
      Long newTurn = GameUtil.switchTurn(playersArr, currentTurnIndex, game.isReverse(), isSkip);
      game.setTurn(newTurn);

      return gameRepository.save(game);
   }

   @Override
   public Game drawCard(Long gameId, Long playerId) {

      Game game = gameRepository
           .findById(gameId)
           .orElseThrow(() -> new GameNotFoundException(gameId));
      // Verifies the game isn't over
      if (game.getWinner() != null) return game;

      // Verifies the player is in the game
      GamePlayer gamePlayer = game.getPlayers().stream()
           .filter(gp -> gp.getPlayer().getId().equals(playerId))
           .findFirst()
           .orElse(null);
      if (gamePlayer == null) throw new PlayerNotFoundException(playerId);

      // Verifies if it's his turn
      if (!(game.getTurn().equals(gamePlayer.getPlayer().getId()))) return game;

      game.deal(1, gamePlayer.getPlayerDeck());
      // TODO: Verificar si esto funciona bien
      // If deck has only one card left, collects all the cards from the playedCards and adds them to deck
      if (game.getDeck().size() <= 1) {
         List<Card> cardsFromPlayedCards = game.getPlayedCards().subList(0, game.getPlayedCards().size() - 1);
         game.getDeck().addAll(cardsFromPlayedCards);
         game.shuffleDeck();
      }

      // Converts LinkedHashSet players to array because it's easier to work with indexes
      GamePlayer[] playersArr = game.getPlayers().toArray(GamePlayer[]::new);
      int currentTurnIndex = IntStream.range(0, playersArr.length)
           .filter(i -> game.getTurn().equals(playersArr[i].getPlayer().getId()))
           .findFirst()
           .orElse(-1);

      Long newTurn = GameUtil.switchTurn(playersArr, currentTurnIndex, game.isReverse(), false);
      game.setTurn(newTurn);

      return gameRepository.save(game);
   }

//   @Override
//   public boolean changed(Long gameId, int[] decksSize) {
//      return (decksSize[0] != game.getPlayerOne().getDeck().size()) ||
//           (decksSize[1] != game.getPlayerTwo().getDeck().size()) ||
//           (decksSize[2] != game.getPlayerThree().getDeck().size());
//   }

   @Override
   public List<Game> findAll() {
      return gameRepository.findAll();
   }

   @Override
   public Game findById(Long id) {
      return gameRepository
           .findById(id)
           .orElseThrow(() -> new GameNotFoundException(id));
   }

   @Override
   public void delete(Long id) {
      Game game = gameRepository
           .findById(id)
           .orElseThrow(() -> new GameNotFoundException(id));
      gameRepository.delete(game);
   }

//   public Game refresh(Long id) {
//      if (game != null && game.getId().equals(id)) return game;
//      return gameRepository
//           .findById(id)
//           .orElseThrow(() -> new GameNotFoundException(id));
//   }
//
//   @Override
//   public void clearGame(Long gameId) {
//      if (game == null) return;
//      if (!game.getId().equals(gameId)) return;
//      game = null;
//   }

}


//   @Override
//   public Game initializeOLD(Long gameId) {
//      // Verifies that game has not been already initialized
//      if (game != null) return game;
//
//      game = gameRepository
//           .findById(gameId)
//           .orElseThrow(() -> new GameNotFoundException(gameId));
//      // Set turn randomly
//      Long id1 = game.getPlayerOne().getId();
//      Long id2 = game.getPlayerTwo().getId();
//      Long id3 = game.getPlayerThree().getId();
//      game.setTurn(RandomId.getRandomId(id1, id2, id3));
//      // Set deck
//      game.setDeck(cardRepository.findAll());
//      // Shuffle deck
//      game.shuffleDeck();
//      // Deal seven cards to each player
//      game.deal(7, game.getPlayerOne().getDeck());
//      game.deal(7, game.getPlayerTwo().getDeck());
//      game.deal(7, game.getPlayerThree().getDeck());
//      // Deal one card to playedCard
//      do {
//         game.deal(1, game.getPlayedCards());
//      } while (GameUtil.getLastCard(game.getPlayedCards()).getColor() == Card.Color.BLACK);
//      game.setCurrentColor(GameUtil.getLastCard(game.getPlayedCards()).getColor());
//
//      return gameRepository.save(game);
//   }


//   public Game playOLD(Long gameId, PlayDTO play) {
//      // Verifies game has been initialized
//      if (!game.getId().equals(gameId)) throw new GameNotInitializedException(gameId);
//      // Verifies the game isn't over
//      if (game.getWinner() != null) return game;
//
//      Player[] players = {game.getPlayerOne(), game.getPlayerTwo(), game.getPlayerThree()};
//      Player player = Arrays.stream(players)
//           .filter(p -> p.getId().equals(play.getPlayerId()))
//           .findFirst()
//           .orElse(null);
//
//      if (player == null) throw new PlayerNotFoundException(play.getPlayerId());
//
//      List<Card> playerCards = player.getDeck();
//
//      // Verifies if it's his turn
//      if (!(game.getTurn().equals(player.getId()))) return game;
//      // Verifies if he has the played card
//      if (!playerCards.contains(play.getCard())) return game;
//
//      Card lastPlayedCard = GameUtil.getLastCard(game.getPlayedCards());
//      Card playedCard = play.getCard();
//
//      // Verifies playedCard has the same Color or Value of the lastPlayedCard
//      if (!(playedCard.getColor().equals(game.getCurrentColor()) ||
//           playedCard.getValue().equals(lastPlayedCard.getValue()) ||
//           playedCard.getColor().equals(Card.Color.BLACK))) {
//         return game;
//      }
//
//      // Adds the play.card to playedCard and removes it from the player's deck
//      game.getPlayedCards().add(playedCard);
//      playerCards.remove(playedCard);
//      // Sets currentColor to the lastPlayed's color
//      game.setCurrentColor(playedCard.getColor());
//      // If the card was the last one of his/her deck, there is a winner and the game ends.
//      if (playerCards.isEmpty()) {
//         game.setWinner(player.getId());
//         gameRepository.save(game);
//         return game;
//      }
//
//      int currentTurnIndex = IntStream.range(0, players.length)
//           .filter(i -> game.getTurn().equals(players[i].getId()))
//           .findFirst()
//           .orElse(-1);
//
//
//      boolean isSkip = false;
//      // Effects of the special cards (SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR)
//      switch (playedCard.getValue()) {
//         case SKIP -> {
//            isSkip = true;
//            break;
//         }
//         case REVERSE -> {
//            game.setReverse(!game.isReverse());
//            break;
//         }
//         case DRAW_TWO -> {
////            Long playerId = players[currentTurnIndex];
//            List<Card> nextPlayerDeck = GameUtil.getNextPlayerDeck(players, currentTurnIndex, game.isReverse());
//            game.deal(2, nextPlayerDeck);
//            break;
//         }
//         case WILD -> {
//            game.setCurrentColor(play.getColor());
//            break;
//         }
//         case WILD_DRAW_FOUR -> {
//            game.setCurrentColor(play.getColor());
//            List<Card> nextPlayerDeck = GameUtil.getNextPlayerDeck(players, currentTurnIndex, game.isReverse());
//            game.deal(4, nextPlayerDeck);
//            break;
//         }
//      }
//      // Sets new Turn based on the last played card
//      Long newTurn = GameUtil.switchTurn(players, currentTurnIndex, game.isReverse(), isSkip);
//      game.setTurn(newTurn);
//
//      return game;
//
//   }

//   @Override
//   public Game drawCardOLD(Long gameId, Long playerId) {
//      // Verifies game has been initialized
//      if (!game.getId().equals(gameId)) throw new GameNotInitializedException(gameId);
//      // Verifies the game isn't over
//      if (game.getWinner() != null) return game;
//
//      Player[] players = {game.getPlayerOne(), game.getPlayerTwo(), game.getPlayerThree()};
//      Player player = Arrays.stream(players)
//           .filter(p -> p.getId().equals(playerId))
//           .findFirst()
//           .orElse(null);
//
//      if (player == null) throw new PlayerNotFoundException(playerId);
//
//      // Verifies if it's his turn
//      if (!(game.getTurn().equals(player.getId()))) return game;
//
//      game.deal(1, player.getDeck());
//      // If deck has only one card left, collects all the cards from the playedCards and adds them to deck
//      if (game.getDeck().size() <= 1) {
//         List<Card> cardsFromPlayedCards = game.getPlayedCards().subList(0, game.getPlayedCards().size() - 1);
//         game.getDeck().addAll(cardsFromPlayedCards);
//         game.shuffleDeck();
//      }
//
//      int currentTurnIndex = IntStream.range(0, players.length)
//           .filter(i -> game.getTurn().equals(players[i].getId()))
//           .findFirst()
//           .orElse(-1);
//
//      Long newTurn = GameUtil.switchTurn(players, currentTurnIndex, game.isReverse(), false);
//      game.setTurn(newTurn);
//
//      return game;
//   }