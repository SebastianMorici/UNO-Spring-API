package com.sebastian.unobackend.player.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SearchGameDTO(@NotNull @Min(2) @Max(10) Integer numberOfPlayers) {
}
