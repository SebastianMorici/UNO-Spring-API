package com.sebastian.unobackend.game;

import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.card.CardRepository;
import com.sebastian.unobackend.game.dto.GameDTO;
import com.sebastian.unobackend.game.dto.GameDTOMapper;
import com.sebastian.unobackend.game.dto.PlayDTO;
import com.sebastian.unobackend.game.util.GameUtil;
import com.sebastian.unobackend.gameplayer.GamePlayer;
import com.sebastian.unobackend.gameplayer.GamePlayerRepository;
import com.sebastian.unobackend.player.Player;
import com.sebastian.unobackend.player.PlayerNotFoundException;
import com.sebastian.unobackend.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GameServiceImpl implements GameService {

   private final GameRepository gameRepository;
   private final CardRepository cardRepository;
   private final GameDTOMapper gameDTOMapper;
   private final GamePlayerRepository gamePlayerRepository;
   private final PlayerRepository playerRepository;

   @Autowired
   public GameServiceImpl(
        GameRepository gameRepository,
        CardRepository cardRepository,
        GameDTOMapper gameDTOMapper,
        GamePlayerRepository gamePlayerRepository,
        PlayerRepository playerRepository
   ) {
      this.gameRepository = gameRepository;
      this.cardRepository = cardRepository;
      this.gameDTOMapper = gameDTOMapper;
      this.gamePlayerRepository = gamePlayerRepository;
      this.playerRepository = playerRepository;
   }


   @Override
   public Game createGame(int numberOfPlayers, Player player) {
      Game newGame = new Game();
      newGame.setNumberOfPlayers(numberOfPlayers);
      Game savedGame = gameRepository.save(newGame);
      savedGame.addPlayer(player);
      GamePlayer gamePlayer = savedGame.getPlayers().stream().findFirst().get();
      gamePlayerRepository.save(gamePlayer);
      playerRepository.save(player);
      return savedGame;
   }

   @Override
   public GameDTO initialize(Long gameId) {
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
      for (GamePlayer gamePlayer : game.getPlayers()) {
         game.deal(7, gamePlayer.getPlayerDeck());
      }
      // Deals one card to playedCards
      List<Card> playedCards = game.getPlayedCards();
      do {
         game.deal(1, playedCards);
      } while (GameUtil.getLastCard(playedCards).getColor() == Card.Color.BLACK);

      game.setCurrentColor(GameUtil.getLastCard(playedCards).getColor());
      game.setCurrentValue(GameUtil.getLastCard(playedCards).getValue());
      return gameDTOMapper.apply(gameRepository.save(game));
   }

   @Override
   public GameDTO play(Long gameId, Long playerId, PlayDTO playDto) {
      Game game = gameRepository
           .findById(gameId)
           .orElseThrow(() -> new GameNotFoundException(gameId));

      if (game.getWinner() != null) return gameDTOMapper.apply(game);

      GamePlayer gamePlayer = game.getPlayers().stream()
           .filter(gp -> gp.getPlayer().getId().equals(playerId))
           .findFirst()
           .orElseThrow(() -> new PlayerNotFoundException(playerId));

      if (!isPlayerTurn(game, gamePlayer)) return gameDTOMapper.apply(game);

      // playedCardToPlayedCards method returns false if something's wrong
      if (!playedCardToPlayedCards(gamePlayer, game, playDto.card())) return gameDTOMapper.apply(game);

      // If the card was the last one, there is a winner and the game ends.
      if (gamePlayer.getPlayerDeck().isEmpty()) {
         game.setWinner(gamePlayer.getPlayer().getId());
         return gameDTOMapper.apply(gameRepository.save(game));
      }

      // Converts LinkedHashSet players to array because it's easier to work with indexes
      GamePlayer[] playersArr = game.getPlayers().toArray(GamePlayer[]::new);
      int currentTurnIndex = IntStream.range(0, playersArr.length)
           .filter(i -> game.getTurn().equals(playersArr[i].getPlayer().getId()))
           .findFirst()
           .orElse(-1);

      // Effect of the special cards (SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR)
      applySpecialCardEffect(game, playDto, playersArr, currentTurnIndex);

      // Sets new Turn based on the last played card
      game.setTurn(GameUtil.switchTurn(playersArr, currentTurnIndex, game.isReverse(), game.isSkip()));

      return gameDTOMapper.apply(gameRepository.save(game));
   }

   @Override
   public GameDTO drawCard(Long gameId, Long playerId) {

      Game game = gameRepository
           .findById(gameId)
           .orElseThrow(() -> new GameNotFoundException(gameId));
      // Verifies the game isn't over
      if (game.getWinner() != null) return gameDTOMapper.apply(game);

      // Verifies the player is in the game
      GamePlayer gamePlayer = game.getPlayers().stream()
           .filter(gp -> gp.getPlayer().getId().equals(playerId))
           .findFirst()
           .orElse(null);
      if (gamePlayer == null) throw new PlayerNotFoundException(playerId);

      // Verifies if it's his turn
      if (!(game.getTurn().equals(gamePlayer.getPlayer().getId()))) return gameDTOMapper.apply(game);

      game.deal(1, gamePlayer.getPlayerDeck());
      // TODO: Check if this works
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

      return gameDTOMapper.apply(gameRepository.save(game));
   }


   @Override
   public List<GameDTO> findAll() {
      return gameRepository
           .findAll()
           .stream()
           .map(gameDTOMapper)
           .collect(Collectors.toList());
   }

   @Override
   public GameDTO findById(Long id) {
      return gameRepository
           .findById(id)
           .map(gameDTOMapper)
           .orElseThrow(() -> new GameNotFoundException(id));
   }

   @Override
   public void delete(Long id) {
      Game game = gameRepository
           .findById(id)
           .orElseThrow(() -> new GameNotFoundException(id));
      gameRepository.delete(game);
   }

   // PRIVATE METHODS
   private boolean isPlayerTurn(Game game, GamePlayer gamePlayer) {
      return game.getTurn().equals(gamePlayer.getPlayer().getId());
   }

   private boolean playedCardToPlayedCards(GamePlayer gamePlayer, Game game, Card cardDTO) {

      List<Card> playerCards = gamePlayer.getPlayerDeck();
      Card playedCard = playerCards.stream()
           .filter(c -> c.equals(cardDTO))
           .findFirst()
           .orElse(null);
      if (playedCard == null) return false;

      // Verifies playedCard has the same Color or Value of the last played card
      if (!(playedCard.getColor().equals(game.getCurrentColor()) ||
           playedCard.getValue().equals(game.getCurrentValue()) ||
           playedCard.getColor().equals(Card.Color.BLACK))) {
         return false;
      }

      // Adds the playedCard to playedCards and removes it from the player's deck
      game.getPlayedCards().add(playedCard);
      playerCards.remove(playedCard);
      // Sets currentColor to the lastPlayed card color
      game.setCurrentColor(playedCard.getColor());
      // Sets currentValue to the lastPlayed card value
      game.setCurrentValue(playedCard.getValue());

      return true;
   }

   private void applySpecialCardEffect(Game game, PlayDTO playDto, GamePlayer[] players, int currentTurnIndex) {
      game.setSkip(false);
      switch (playDto.card().getValue()) {
         case SKIP -> {
            game.setSkip(true);
            break;
         }
         case REVERSE -> {
            game.setReverse(!game.isReverse());
            break;
         }
         case DRAW_TWO -> {
            List<Card> nextPlayerDeck = GameUtil.getNextPlayerDeck(players, currentTurnIndex, game.isReverse());
            game.deal(2, nextPlayerDeck);
            break;
         }
         case WILD -> {
            game.setCurrentColor(playDto.color());
            break;
         }
         case WILD_DRAW_FOUR -> {
            game.setCurrentColor(playDto.color());
            List<Card> nextPlayerDeck = GameUtil.getNextPlayerDeck(players, currentTurnIndex, game.isReverse());
            game.deal(4, nextPlayerDeck);
            break;
         }
      }

   }

}
//   public Game refresh(Long id) {
//      if (game != null && game.getId().equals(id)) return game;
//      return gameRepository
//           .findById(id)
//           .orElseThrow(() -> new GameNotFoundException(id));
//   }

//   @Override
//   public boolean changed(Long gameId, int[] decksSize) {
//      return (decksSize[0] != game.getPlayerOne().getDeck().size()) ||
//           (decksSize[1] != game.getPlayerTwo().getDeck().size()) ||
//           (decksSize[2] != game.getPlayerThree().getDeck().size());
//   }
