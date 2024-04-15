package com.example.foodx_be.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (username == null || username.isEmpty() || username.length() < 2) {
            return false;
        }

        for (char ch : username.toCharArray()) {
            if (!Character.isLetter(ch) && ch != ' ') {
                return false;
            }
        }

        return true;
    }
}
