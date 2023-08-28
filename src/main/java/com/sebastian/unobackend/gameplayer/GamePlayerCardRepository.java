package com.sebastian.unobackend.gameplayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayerCardRepository extends JpaRepository<GamePlayerCard, GamePlayerCardId> {
}
