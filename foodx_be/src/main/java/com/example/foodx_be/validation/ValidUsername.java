package com.example.foodx_be.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface ValidUsername {
    String message() default "Username must be 5 - 10 characters long, cannot contain special characters and cannot contain capital words";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
