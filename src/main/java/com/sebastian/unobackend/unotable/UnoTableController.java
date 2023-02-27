package com.sebastian.unobackend.unotable;

import com.sebastian.unobackend.unotable.util.ChangedRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
public class UnoTableController {

    private final UnoTableService unoTableService;
    private final Lock lock = new ReentrantLock();

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
        try {
            lock.lock();
            return ResponseEntity.status(HttpStatus.OK).body(unoTableService.initialize(id));
        } finally {
            lock.unlock();
        }
    }

    @DeleteMapping("/uno_tables/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        unoTableService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/uno_tables/{id}/play")
    public ResponseEntity<UnoTable> play(@PathVariable Long id, @RequestBody Play play) {
        return ResponseEntity.status(HttpStatus.OK).body(unoTableService.play(id, play));
    }

    @GetMapping("/uno_tables/{id}/players/{playerId}/draw")
    public ResponseEntity<UnoTable> drawCard(@PathVariable Long id, @PathVariable Long playerId) {
        return ResponseEntity.status(HttpStatus.OK).body(unoTableService.drawCard(id, playerId));
    }

    @GetMapping("/uno_tables/{id}/refresh")
    public ResponseEntity<UnoTable> refresh(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(unoTableService.refresh(id));
    }

    @PostMapping("/uno_tables/{id}/changed")
    public ResponseEntity<String> changed(@PathVariable Long id, @RequestBody ChangedRequestData requestData) {
        return ResponseEntity.status(HttpStatus.OK).body("{ \"changed\": \"" + unoTableService.changed(id, requestData.getDecksSize()) + "\" }" );
    }

    @PostMapping("/uno_tables/{id}/clear")
    public ResponseEntity<Void> clear(@PathVariable Long id) {
        unoTableService.clearUnoTable(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
