package com.sebastian.unobackend.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
public class GameController {

    private final GameService gameService;
    private final Lock lock = new ReentrantLock();

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/uno_tables")
    public ResponseEntity<List<Game>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findAll());
    }

    @GetMapping("/uno_tables/{id}")
    public ResponseEntity<Game> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findById(id));
    }

    @PostMapping("/uno_tables/{id}/initialize")
    public ResponseEntity<Game> initialize(@PathVariable Long id) {
        try {
            lock.lock();
            return ResponseEntity.status(HttpStatus.OK).body(gameService.initialize(id));
        } finally {
            lock.unlock();
        }
    }

    @DeleteMapping("/uno_tables/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/uno_tables/{id}/players/{playerId}/play")
    public ResponseEntity<Game> play(@PathVariable Long id, @PathVariable Long playerId, @RequestBody PlayDTO playDto) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.play(id, playerId, playDto));
    }

    @GetMapping("/uno_tables/{id}/players/{playerId}/draw")
    public ResponseEntity<Game> drawCard(@PathVariable Long id, @PathVariable Long playerId) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.drawCard(id, playerId));
    }

//    @GetMapping("/uno_tables/{id}/refresh")
//    public ResponseEntity<Game> refresh(@PathVariable Long id) {
//        return ResponseEntity.status(HttpStatus.OK).body(gameService.refresh(id));
//    }

//    @PostMapping("/uno_tables/{id}/changed")
//    public ResponseEntity<String> changed(@PathVariable Long id, @RequestBody ChangedRequestData requestData) {
//        return ResponseEntity.status(HttpStatus.OK).body("{ \"changed\": \"" + gameService.changed(id, requestData.getDecksSize()) + "\" }" );
//    }

//    @PostMapping("/uno_tables/{id}/clear")
//    public ResponseEntity<Void> clear(@PathVariable Long id) {
//        gameService.clearGame(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

}
