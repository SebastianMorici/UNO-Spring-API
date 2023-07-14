package com.sebastian.unobackend.player.dto;

import com.sebastian.unobackend.player.Player;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PlayerDTOMapper implements Function<Player, PlayerDTO> {
    @Override
    public PlayerDTO apply(Player player) {
        return new PlayerDTO(player.getId(), player.getFirstname(), player.getLastname(), player.getUsername());
    }
}
