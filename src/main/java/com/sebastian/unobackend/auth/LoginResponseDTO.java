package com.sebastian.unobackend.auth;

import com.sebastian.unobackend.player.Player;

public record LoginResponseDTO(Player player, String jwt) {
}
