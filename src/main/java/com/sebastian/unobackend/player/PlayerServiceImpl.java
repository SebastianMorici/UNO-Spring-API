package com.sebastian.unobackend.player;

import com.sebastian.unobackend.table.Table;
import com.sebastian.unobackend.table.TableRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TableRepository tableRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TableRepository tableRepository) {
        this.playerRepository = playerRepository;
        this.tableRepository = tableRepository;
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

    public Table searchGame() {
//        Table table = tableRepository.findByIsFull(false).orElse(new Table());
        return null;
    }
}
