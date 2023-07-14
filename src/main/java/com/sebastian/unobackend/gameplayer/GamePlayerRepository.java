package com.sebastian.unobackend.gameplayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, GamePlayerId> {

}
