package com.sebastian.unobackend.unotable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UnoTableController {

    private final UnoTableService unoTableService;

    @Autowired
    public UnoTableController(UnoTableService unoTableService) {
        this.unoTableService = unoTableService;
    }

    @GetMapping("/uno_tables")
    public ResponseEntity<List<UnoTable>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(unoTableService.findAll());
    }

    @GetMapping("/uno_tables/{id}")
    public ResponseEntity<UnoTable> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(unoTableService.findById(id));
    }

    @PostMapping("/uno_tables/{id}/initialize")
    public ResponseEntity<UnoTable> initialize(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(unoTableService.initialize(id));
    }



}
