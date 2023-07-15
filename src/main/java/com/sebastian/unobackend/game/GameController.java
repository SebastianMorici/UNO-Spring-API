package com.sebastian.unobackend.game;

import com.sebastian.unobackend.game.dto.GameDTO;
import com.sebastian.unobackend.game.dto.PlayDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    private final GameService gameService;
    private final Lock lock = new ReentrantLock();

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public ResponseEntity<List<GameDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findById(id));
    }

    @PostMapping("/{id}/initialize")
    public ResponseEntity<GameDTO> initialize(@PathVariable Long id) {
        try {
            lock.lock();
            return ResponseEntity.status(HttpStatus.OK).body(gameService.initialize(id));
        } finally {
            lock.unlock();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/players/{playerId}/play")
    public ResponseEntity<GameDTO> play(@PathVariable Long id, @PathVariable Long playerId, @Valid @RequestBody PlayDTO playDto) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.play(id, playerId, playDto));
    }

    @GetMapping("/{id}/players/{playerId}/draw")
    public ResponseEntity<GameDTO> drawCard(@PathVariable Long id, @PathVariable Long playerId) {
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
