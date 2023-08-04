package com.sebastian.unobackend.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableMethodSecurity
public class JwtSecurityConfiguration {

   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
           .authorizeHttpRequests(requests -> {
              requests.requestMatchers(
                   "/api/v1/auth/**",
                   "/v2/api-docs",
                   "/v3/api-docs",
                   "/v3/api-docs/**",
                   "/swagger-resources",
                   "/swagger-resources/**",
                   "/configuration/ui",
                   "/configuration/security",
                   "/swagger-ui/**",
                   "/webjars/**",
                   "/swagger-ui.html"
              ).permitAll();
              requests.anyRequest().authenticated();
//                    requests.anyRequest().permitAll();
           })
           .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
           .csrf(AbstractHttpConfigurer::disable)
           .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
           .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
           );
      return http.build();
   }

   @Bean
   WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
         public void addCorsMappings(@NotNull CorsRegistry registry) {
            registry
                 .addMapping("/**")
                 .allowedMethods("*")
                 .allowedOrigins("http://localhost:3000");
         }
      };
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public KeyPair keyPair() throws NoSuchAlgorithmException {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      return keyPairGenerator.generateKeyPair();
   }

   @Bean
   public RSAKey rsaKey(KeyPair keyPair) {
      return new RSAKey
           .Builder((RSAPublicKey) keyPair.getPublic())
           .privateKey(keyPair.getPrivate())
           .keyID(UUID.randomUUID().toString())
           .build();
   }

   @Bean
   public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
      JWKSet jwkSet = new JWKSet(rsaKey);
      return (jwkSelector, context) -> jwkSelector.select(jwkSet);
   }

   @Bean
   public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
      return NimbusJwtDecoder
           .withPublicKey(rsaKey.toRSAPublicKey())
           .build();
   }

   @Bean
   public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
      return new NimbusJwtEncoder(jwkSource);
   }

   @Bean
   public AuthenticationManager authManager(UserDetailsService detailsService) {
      DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
      daoAuthenticationProvider.setUserDetailsService(detailsService);
      daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
      return new ProviderManager(daoAuthenticationProvider);
   }

   @Bean
   public JwtAuthenticationConverter jwtAuthenticationConverter() {
      JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
      jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
      jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
      JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
      jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
      return jwtConverter;
   }
}
