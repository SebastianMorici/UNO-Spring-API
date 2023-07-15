package com.sebastian.unobackend.player;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UsernameValidator implements ConstraintValidator<UniqueUsername, String> {

   PlayerRepository playerRepository;

   @Autowired
   public UsernameValidator(PlayerRepository playerRepository) {
      this.playerRepository = playerRepository;
   }

   @Override
   public void initialize(UniqueUsername constraintAnnotation) {
   }

   @Override
   public boolean isValid(String usernameField, ConstraintValidatorContext context) {
      return playerRepository.findByUsername(usernameField).isEmpty();
   }
}
