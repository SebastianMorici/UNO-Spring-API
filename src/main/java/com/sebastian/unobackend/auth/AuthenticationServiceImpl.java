package com.sebastian.unobackend.auth;

import com.sebastian.unobackend.auth.dto.LoginResponseDTO;
import com.sebastian.unobackend.player.*;
import com.sebastian.unobackend.player.dto.PlayerDTO;
import com.sebastian.unobackend.player.dto.PlayerDTOMapper;
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
    private final PlayerDTOMapper playerDTOMapper;

    @Autowired
    public AuthenticationServiceImpl(PlayerRepository playerRepository, RoleRepository roleRepository, PasswordEncoder encoder, AuthenticationManager authenticationManager, TokenService tokenService, PlayerDTOMapper playerDTOMapper) {
        this.playerRepository = playerRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.playerDTOMapper = playerDTOMapper;
    }

    //Todo: Make registrationDTO
    @Override
    public PlayerDTO registerPlayer(String firstname, String lastname, String username, String password) {

        Role role = roleRepository.findByAuthority(Authority.USER).get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(role);

        Player registeredPlayer = new Player(firstname, lastname, username, encoder.encode(password), authorities);
        return playerDTOMapper.apply(playerRepository.save(registeredPlayer));
    }

    @Override
    public LoginResponseDTO loginPlayer(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = tokenService.generateToken(auth);
        Player loginPlayer = playerRepository
                .findByUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException(username));
        return new LoginResponseDTO(playerDTOMapper.apply(loginPlayer), token);
    }
}
