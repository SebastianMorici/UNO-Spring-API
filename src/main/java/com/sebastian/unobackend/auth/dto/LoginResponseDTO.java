package com.sebastian.unobackend.auth.dto;

import com.sebastian.unobackend.player.dto.PlayerDTO;

public record LoginResponseDTO(PlayerDTO player, String jwt) {
}
