package com.sebastian.unobackend.game;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByIsFull(boolean isFull);
}
