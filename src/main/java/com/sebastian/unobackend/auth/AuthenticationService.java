package com.sebastian.unobackend.auth;

import com.sebastian.unobackend.player.Player;

public interface AuthenticationService {
    Player registerPlayer(String firstname, String lastname, String username, String password);
    LoginResponseDTO loginPlayer(String username, String password);
}
