package com.sebastian.unobackend.unotable;

public class UnoTableNotInitializedException extends RuntimeException {

    public UnoTableNotInitializedException(Long id) {
        super("UNO table with id: " + id + " has not been initialized");
    }
}
