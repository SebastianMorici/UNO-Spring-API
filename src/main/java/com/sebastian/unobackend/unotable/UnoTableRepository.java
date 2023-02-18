package com.sebastian.unobackend.unotable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnoTableRepository extends JpaRepository<UnoTable, Long> {
    List<UnoTable> findByIsFull(boolean isFull);
}
