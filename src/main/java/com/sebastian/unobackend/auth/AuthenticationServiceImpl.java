package com.sebastian.unobackend.auth;

import com.sebastian.unobackend.player.Player;
import com.sebastian.unobackend.player.PlayerRepository;
import com.sebastian.unobackend.player.Role;
import com.sebastian.unobackend.player.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final PlayerRepository playerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AuthenticationServiceImpl(PlayerRepository playerRepository, RoleRepository roleRepository, PasswordEncoder encoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.playerRepository = playerRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    //Todo: Make registrationDTO
    @Override
    public Player registerPlayer(String firstname, String lastname, String username, String password) {

        Role role = roleRepository.findByAuthority("USER").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(role);
        return playerRepository.save(new Player(firstname, lastname, username, encoder.encode(password), authorities));
    }

    @Override
    public LoginResponseDTO loginPlayer(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = tokenService.generateToken(auth);
        return new LoginResponseDTO(playerRepository.findByUsername(username).get(), token);
    }
}
