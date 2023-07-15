package com.sebastian.unobackend.auth.dto;

import com.sebastian.unobackend.player.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationDTO(
        @NotNull @Size(min = 2, max = 15) String firstname,
        @NotNull @Size(min = 2, max = 15) String lastname,
        @NotNull @UniqueUsername @Email String username,
        @NotNull @Size(min = 4) String password
) {
}
