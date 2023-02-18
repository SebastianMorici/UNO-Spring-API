package com.sebastian.unobackend.unotable;

import java.util.List;

public interface UnoTableService {
    UnoTable initialize(Long unoTableId) throws UnoTableNotFoundException;
    List<UnoTable> findAll();
    UnoTable findById(Long id) throws UnoTableNotFoundException;
}
