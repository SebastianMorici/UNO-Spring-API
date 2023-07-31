package com.sebastian.unobackend.auth;

import com.sebastian.unobackend.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

   private final JwtEncoder jwtEncoder;

   @Autowired
   public TokenServiceImpl(JwtEncoder jwtEncoder) {
      this.jwtEncoder = jwtEncoder;
   }

   @Override
   public String generateToken(Authentication authentication) {
      JwtClaimsSet claims = JwtClaimsSet
           .builder()
           .issuer("self")
           .issuedAt(Instant.now())
           .expiresAt(Instant.now().plusSeconds(60 * 30))
           .subject(authentication.getName())
           .claim("scope", createScope(authentication))
           .claim("id", ((Player) authentication.getPrincipal()).getId())
           .build();
      return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
   }

   @Override
   public String createScope(Authentication authentication) {
      return authentication.getAuthorities().stream()
           .map(GrantedAuthority::getAuthority)
           .collect(Collectors.joining(" "));
   }
}
