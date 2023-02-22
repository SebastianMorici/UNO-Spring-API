package com.sebastian.unobackend.unotable;

import com.sebastian.unobackend.card.Card;
import com.sebastian.unobackend.card.CardRepository;
import com.sebastian.unobackend.player.Player;
import com.sebastian.unobackend.player.PlayerNotFoundException;
import com.sebastian.unobackend.player.PlayerRepository;
import com.sebastian.unobackend.player.util.RandomId;
import com.sebastian.unobackend.unotable.util.UnoTableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnoTableServiceImpl implements UnoTableService {

    private final UnoTableRepository unoTableRepository;
    private final PlayerRepository playerRepository;
    private final CardRepository cardRepository;
    private UnoTable unoTable;

    @Autowired
    public UnoTableServiceImpl(UnoTableRepository unoTableRepository, PlayerRepository playerRepository, CardRepository cardRepository) {
        this.unoTableRepository = unoTableRepository;
        this.playerRepository = playerRepository;
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
        unoTable.deal(7, unoTable.getPlayerOneCards());
        unoTable.deal(7, unoTable.getPlayerTwoCards());
        unoTable.deal(7, unoTable.getPlayerThreeCards());
        // Deal one card to playedCard
        do {
            unoTable.deal(1, unoTable.getPlayedCards());
        } while(UnoTableUtil.getLastCard(unoTable.getPlayedCards()).getColor() == Card.Color.BLACK);

        return unoTableRepository.save(unoTable);
    }

    @Override
    public UnoTable play(Long unoTableId, Play play) {
        // Verifies unoTable has been initialized
        if (!unoTable.getId().equals(unoTableId)) throw new UnoTableNotInitializedException(unoTableId);

        Player player = playerRepository
                .findById(play.getPlayerId())
                .orElseThrow(() -> new PlayerNotFoundException(play.getPlayerId()));
        List<Card> playerCards = null;

        // Verifies if it's his turn
        if (!(unoTable.getTurn().equals(player.getId()))) return unoTable;
        // Verifies if he has the played card
        if (player.getId().equals(unoTable.getPlayerOne().getId())) {
            playerCards = unoTable.getPlayerOneCards();
            if (!playerCards.contains(play.getCard())) return unoTable;
        }
        if (player.getId().equals(unoTable.getPlayerTwo().getId())) {
            playerCards = unoTable.getPlayerTwoCards();
            if (!playerCards.contains(play.getCard())) return unoTable;
        }
        if (player.getId().equals(unoTable.getPlayerThree().getId())) {
            playerCards = unoTable.getPlayerThreeCards();
            if (!playerCards.contains(play.getCard())) return unoTable;
        }

        Card lastPlayedCard = UnoTableUtil.getLastCard(unoTable.getPlayedCards());
        Card playedCard = play.getCard();

        // Verifies playedCard has the same Color or Value of the lastPlayedCard
        if (!(playedCard.getColor().equals(lastPlayedCard.getColor()) || playedCard.getValue().equals(lastPlayedCard.getValue()) || playedCard.getColor().equals(Card.Color.BLACK))) {
            return unoTable;
        }

        // Switches turn
        unoTable.setTurn((unoTable.getTurn() + 1) % 3);
        switch (playedCard.getValue()) {
            case SKIP -> {
                unoTable.setTurn((unoTable.getTurn() + 2) % 3);
                break;
            }
            case REVERSE -> {
                unoTable.setReverse(!unoTable.isReverse());
                break;
            }
            case DRAW_TWO -> {
                break;
            }
        }

        unoTable.getPlayedCards().add(playedCard);
        playerCards.remove(playedCard);

        return unoTable;

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

    public UnoTable refresh(Long id) {
        if (unoTable != null && unoTable.getId().equals(id)) return unoTable;
        return unoTableRepository
                .findById(id)
                .orElseThrow(() -> new UnoTableNotFoundException(id));
    }


}
