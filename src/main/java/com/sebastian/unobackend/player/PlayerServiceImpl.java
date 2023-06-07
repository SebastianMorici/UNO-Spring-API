package com.sebastian.unobackend.player;

import com.sebastian.unobackend.association.GamePlayer;
import com.sebastian.unobackend.association.GamePlayerId;
import com.sebastian.unobackend.association.GamePlayerNotFoundException;
import com.sebastian.unobackend.association.GamePlayerRepository;
import com.sebastian.unobackend.game.Game;
import com.sebastian.unobackend.game.GameRepository;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

   private final PlayerRepository playerRepository;
   private final GameRepository gameRepository;
   private final GamePlayerRepository gamePlayerRepository;

   @Autowired
   public PlayerServiceImpl(PlayerRepository playerRepository, GameRepository gameRepository,
                            GamePlayerRepository gamePlayerRepository) {
      this.playerRepository = playerRepository;
      this.gameRepository = gameRepository;
      this.gamePlayerRepository = gamePlayerRepository;
   }

   @Override
   public List<Player> findAll() {
      return playerRepository.findAll();
   }

   @Override
   public Player findById(Long id) {
      return playerRepository
           .findById(id)
           .orElseThrow(() -> new PlayerNotFoundException(id));
   }

   @Override
   public Player create(Player newPlayer) {
      return playerRepository.save(newPlayer);
   }

   @Override
   public Player update(Long id, Player updatedPlayer) {
      Player player = playerRepository
           .findById(id)
           .orElseThrow(() -> new PlayerNotFoundException(id));
      BeanUtils.copyProperties(updatedPlayer, player, "id");
      return playerRepository.save(player);
   }

   @Override
   public void delete(Long id) {
      Player player = playerRepository
           .findById(id)
           .orElseThrow(() -> new PlayerNotFoundException(id));
      playerRepository.delete(player);
   }

   @Override
   public Player login(Player loginPlayer) {
      return playerRepository
           .findByName(loginPlayer.getName())
           .orElseThrow(() -> new PlayerNotFoundException(loginPlayer.getName()));
   }

   public Game searchGame(Long playerId, SearchGameDTO searchGameDTO) {
      Player player = playerRepository
           .findById(playerId)
           .orElseThrow(() -> new PlayerNotFoundException(playerId));

      List<Game> nonFullGames = gameRepository.findByIsFull(false);
      // Creates a new game and adds the first player
      if (nonFullGames.isEmpty()) {
         Game newGame = new Game();
         newGame.setNumberOfPlayers(searchGameDTO.numberOfPlayers());
         Game savedGame = gameRepository.save(newGame);
         savedGame.addPlayer(player);
         GamePlayer gamePlayer = savedGame.getPlayers().stream()
              .filter(gp -> gp.getPlayer().getId().equals(playerId))
              .findFirst()
              .orElseThrow(() -> new GamePlayerNotFoundException(savedGame.getId(), playerId));
         gamePlayerRepository.save(gamePlayer);
         playerRepository.save(player);
         return gameRepository.save(savedGame);
      }
      // Filters the games that matches the numberOfPlayers argument
      List<Game> gamesToJoin = nonFullGames.stream()
           .filter(game -> game.getNumberOfPlayers() == searchGameDTO.numberOfPlayers())
           .toList();
      // game is the last of gamesToJoin
      Game game = gamesToJoin.get(gamesToJoin.size() - 1);
      // Adds the new player to the game
      game.addPlayer(player);
      playerRepository.save(player);
      if (game.getPlayers().size() == game.getNumberOfPlayers()) game.setFull(true);
      return gameRepository.save(game);
   }


}
//   public Game searchGameOLD(Long playerId) {
//      Player player = playerRepository
//           .findById(playerId)
//           .orElseThrow(() -> new PlayerNotFoundException(playerId));
//
//      List<Game> nonFullGames = gameRepository.findByIsFull(false);
//      // Creates a new Game and set playerOne
//      if (nonFullGames.isEmpty()) {
//         Game newGame = new Game();
//         newGame.setPlayerOne(player);
//         return gameRepository.save(newGame);
//      }
//      // game is the last of nonFullGames
//      Game game = nonFullGames.get(nonFullGames.size() - 1);
//      // Set playerTwo
//      if (game.getPlayerTwo() == null) {
//         game.setPlayerTwo(player);
//         return gameRepository.save(game);
//      }
//      // Set playerThree
//      game.setPlayerThree(player);
//      // isFull = true
//      game.setFull(true);
//
//      return gameRepository.save(game);
//   }

