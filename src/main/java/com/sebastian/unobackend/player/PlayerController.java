package com.sebastian.unobackend.player;

import com.sebastian.unobackend.game.dto.GameDTO;
import com.sebastian.unobackend.player.dto.SearchGameDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
//@SecurityRequirement(name = "bearerAuth")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping()
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Player>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(playerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(playerService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> update(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        return ResponseEntity.status(HttpStatus.OK).body(playerService.update(id, updatedPlayer));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('USER') and #playerId == principal.claims['id']")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{playerId}/search_game")
//    @PreAuthorize("hasRole('USER') and #playerId == principal.claims['id']")
    public ResponseEntity<GameDTO> searchGame(@PathVariable Long playerId, @Valid @RequestBody SearchGameDTO searchGameDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(playerService.searchGame(playerId, searchGameDTO));
    }

}
