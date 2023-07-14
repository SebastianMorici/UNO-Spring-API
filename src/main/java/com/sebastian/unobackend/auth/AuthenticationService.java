package com.sebastian.unobackend.auth;

import com.sebastian.unobackend.auth.dto.LoginResponseDTO;
import com.sebastian.unobackend.player.dto.PlayerDTO;

public interface AuthenticationService {
    PlayerDTO registerPlayer(String firstname, String lastname, String username, String password);
    LoginResponseDTO loginPlayer(String username, String password);
}
