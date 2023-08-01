package com.sebastian.unobackend.auth;

import com.sebastian.unobackend.auth.dto.LoginDTO;
import com.sebastian.unobackend.auth.dto.LoginResponseDTO;
import com.sebastian.unobackend.auth.dto.RegistrationDTO;
import com.sebastian.unobackend.player.dto.PlayerDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<PlayerDTO> registerPlayer(@Valid @RequestBody RegistrationDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authenticationService.registerPlayer(
                        dto.firstname(),
                        dto.lastname(),
                        dto.username(),
                        dto.password())
                );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginPlayer(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.loginPlayer(dto.username(), dto.password()));
    }
}
