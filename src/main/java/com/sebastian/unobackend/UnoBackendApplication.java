package com.sebastian.unobackend;

import com.sebastian.unobackend.player.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class UnoBackendApplication {

   public static void main(String[] args) {
      SpringApplication.run(UnoBackendApplication.class, args);
   }

   @Bean
   CommandLineRunner run(RoleRepository roleRepository, PlayerRepository playerRepository, PasswordEncoder encoder) {
      return args -> {
         if(roleRepository.findByAuthority(Authority.ADMIN).isPresent()) return;

         Role adminRole = roleRepository.save(new Role(Authority.ADMIN));
         roleRepository.save(new Role(Authority.USER));

         Set<Role> authorities = new HashSet<>();
         authorities.add(adminRole);
         Player admin = new Player("admin", "admin", "admin", encoder.encode("password"), authorities);
         playerRepository.save(admin);
      };
   }

}
