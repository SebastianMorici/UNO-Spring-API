package com.sebastian.unobackend.auth;

import org.springframework.security.core.Authentication;

public interface TokenService {

    String generateToken(Authentication authentication);
    String createScope(Authentication authentication);
}
