package com.sebastian.unobackend.unotable;

public class UnoTableNotFoundException extends RuntimeException {
    public UnoTableNotFoundException(Long id) {
        super("UNO table with id: " + id + " not found");
    }
}
