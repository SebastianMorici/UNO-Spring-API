package com.sebastian.unobackend.player;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
   String message() default "Username already exists";
   Class<?>[] groups() default {};
   Class<? extends Payload>[] payload() default {};
}
