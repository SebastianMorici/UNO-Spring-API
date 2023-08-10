package com.sebastian.unobackend.player;

import com.sebastian.unobackend.game.Game;
import com.sebastian.unobackend.game.GameRepository;
import com.sebastian.unobackend.game.GameService;
import com.sebastian.unobackend.game.dto.GameDTO;
import com.sebastian.unobackend.game.dto.GameDTOMapper;
import com.sebastian.unobackend.gameplayer.GamePlayer;
import com.sebastian.unobackend.gameplayer.GamePlayerRepository;
import com.sebastian.unobackend.player.dto.SearchGameDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

   private final PlayerRepository playerRepository;
   private final GameRepository gameRepository;
   private final GamePlayerRepository gamePlayerRepository;
   private final GameDTOMapper gameDTOMapper;
   private final GameService gameService;

   @Autowired
   public PlayerServiceImpl(
        PlayerRepository playerRepository,
        GameRepository gameRepository,
        GamePlayerRepository gamePlayerRepository,
        GameDTOMapper gameDTOMapper,
        GameService gameService
   ) {
      this.playerRepository = playerRepository;
      this.gameRepository = gameRepository;
      this.gamePlayerRepository = gamePlayerRepository;
      this.gameDTOMapper = gameDTOMapper;
      this.gameService = gameService;
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

   public GameDTO searchGame(Long playerId, SearchGameDTO searchGameDTO) {
      Player player = playerRepository
           .findById(playerId)
           .orElseThrow(() -> new PlayerNotFoundException(playerId));

      List<Game> nonFullGames = gameRepository.findByIsFull(false);
      if (nonFullGames.isEmpty()) {
         Game newGame = gameService.createGame(searchGameDTO.numberOfPlayers(), player);
         return gameDTOMapper.apply(gameRepository.save(newGame));
      }

      List<Game> gamesToJoin = nonFullGames.stream()
           .filter(game -> game.getNumberOfPlayers() == searchGameDTO.numberOfPlayers())
           .toList();

      Game game = gamesToJoin.get(gamesToJoin.size() - 1);

      if (game.getPlayers().stream().anyMatch(gp -> gp.getPlayer().equals(player))) return gameDTOMapper.apply(game);

      game.addPlayer(player);
      playerRepository.save(player);
      if (game.getPlayers().size() == game.getNumberOfPlayers()) {
         game.setFull(true);
         gameService.initialize(game.getId());
      }
      return gameDTOMapper.apply(gameRepository.save(game));
   }


   @Override
   public UserDetails loadUserByUsername(String username) throws PlayerNotFoundException {
      return playerRepository
           .findByUsername(username)
           .orElseThrow(() -> new PlayerNotFoundException(username));
   }

}

