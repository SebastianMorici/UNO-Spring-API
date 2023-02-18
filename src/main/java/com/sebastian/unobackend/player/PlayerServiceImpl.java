package com.sebastian.unobackend.player;

import com.sebastian.unobackend.player.util.RandomId;
import com.sebastian.unobackend.unotable.UnoTable;
import com.sebastian.unobackend.unotable.UnoTableRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final UnoTableRepository unoTableRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, UnoTableRepository unoTableRepository) {
        this.playerRepository = playerRepository;
        this.unoTableRepository = unoTableRepository;
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

    public Long searchGame(Long playerId) {
        Player player = playerRepository
                .findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

        List<UnoTable> nonFullUnoTables = unoTableRepository.findByIsFull(false);
        // Creates a new Table and set playerOne
        if (nonFullUnoTables.isEmpty()){
            UnoTable newUnoTable = new UnoTable();
            newUnoTable.setPlayerOne(player);
            return unoTableRepository.save(newUnoTable).getId();
        }
        // table is the las of nonFullTables
        UnoTable unoTable = nonFullUnoTables.get(nonFullUnoTables.size() - 1);
        // Set playerTwo
        if (unoTable.getPlayerTwo() == null) {
            unoTable.setPlayerTwo(player);
            return unoTableRepository.save(unoTable).getId();
        }
        // Set playerThree
        unoTable.setPlayerThree(player);
        // isFull = true
        unoTable.setFull(true);
//        // Set turn randomly
//        Long id1 = unoTable.getPlayerOne().getId();
//        Long id2 = unoTable.getPlayerTwo().getId();
//        Long id3 = unoTable.getPlayerThree().getId();
//        unoTable.setTurn(RandomId.getRandomId(id1, id2, id3));
        return unoTableRepository.save(unoTable).getId();
    }
}


