package com.sebastian.unobackend.unotable;

import com.sebastian.unobackend.player.util.RandomId;
import com.sebastian.unobackend.unotable.util.UnoTableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnoTableServiceImpl implements UnoTableService {

    private final UnoTableRepository unoTableRepository;

    @Autowired
    public UnoTableServiceImpl(UnoTableRepository unoTableRepository) {
        this.unoTableRepository = unoTableRepository;
    }

    @Override
    public UnoTable initialize(Long unoTableId) throws UnoTableNotFoundException {
        UnoTable unoTable = unoTableRepository
                .findById(unoTableId)
                .orElseThrow(() -> new UnoTableNotFoundException(unoTableId));
        // Set turn randomly
        Long id1 = unoTable.getPlayerOne().getId();
        Long id2 = unoTable.getPlayerTwo().getId();
        Long id3 = unoTable.getPlayerThree().getId();
        unoTable.setTurn(RandomId.getRandomId(id1, id2, id3));
        // Shuffle deck
        unoTable.getDeck().shuffle();
        // Deal seven cards to each player
        unoTable.getDeck().deal(7, unoTable.getPlayerOneCards());
        unoTable.getDeck().deal(7, unoTable.getPlayerTwoCards());
        unoTable.getDeck().deal(7, unoTable.getPlayerThreeCards());
        // Deal one card to playedCard
        do {
            unoTable.getDeck().deal(1, unoTable.getPlayedCards());
        } while(UnoTableUtil.getLastCard(unoTable.getPlayedCards()).getColor() == Card.Color.BLACK);

        return unoTableRepository.save(unoTable);
    }

    @Override
    public List<UnoTable> findAll() {
        return unoTableRepository.findAll();
    }

    @Override
    public UnoTable findById(Long id){
        return unoTableRepository
                .findById(id)
                .orElseThrow(() -> new UnoTableNotFoundException(id));
    }
}
