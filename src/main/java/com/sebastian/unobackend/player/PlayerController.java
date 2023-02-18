package com.sebastian.unobackend.player;

import com.sebastian.unobackend.unotable.UnoTable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(playerService.findAll());
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(playerService.findById(id));
    }

    @PostMapping("/players")
    public ResponseEntity<Player> create(@Valid @RequestBody Player newPlayer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.create(newPlayer));
    }


    @PutMapping("/players/{id}")
    public ResponseEntity<Player> update(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        return ResponseEntity.status(HttpStatus.OK).body(playerService.update(id, updatedPlayer));
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/players/login")
    public ResponseEntity<Player> login(@RequestBody Player loginPlayer) {
        return ResponseEntity.status(HttpStatus.OK).body(playerService.login(loginPlayer));
    }

    @PostMapping("/players/{playerId}/search_game")
    public ResponseEntity<String> searchGame(@PathVariable Long playerId) {
        return ResponseEntity.status(HttpStatus.OK).body("{ \"id\": " + playerService.searchGame(playerId) + " }");
    }


}
