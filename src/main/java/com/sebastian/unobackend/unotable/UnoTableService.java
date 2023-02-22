package com.sebastian.unobackend.unotable;

import com.sebastian.unobackend.player.PlayerNotFoundException;

import java.util.List;

public interface UnoTableService {
    List<UnoTable> findAll();
    UnoTable findById(Long id) throws UnoTableNotFoundException;
    UnoTable initialize(Long unoTableId) throws UnoTableNotFoundException;
    UnoTable play(Long unoTableId, Play play) throws UnoTableNotFoundException, PlayerNotFoundException;
    UnoTable refresh(Long id) throws UnoTableNotFoundException;
}
