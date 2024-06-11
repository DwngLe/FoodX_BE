package com.example.foodx_be.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface ValidUsername {
    String message() default
            "Username must be 5 - 10 characters long, cannot contain special characters and cannot contain capital words";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
