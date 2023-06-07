package com.sebastian.unobackend.association;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, GamePlayerId> {

}
