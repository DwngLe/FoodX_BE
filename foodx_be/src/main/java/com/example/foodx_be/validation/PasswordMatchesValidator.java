package com.example.foodx_be.validation;

import com.example.foodx_be.dto.RegisterCommand;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        RegisterCommand registerCommand = (RegisterCommand) obj;
        return registerCommand.getPassword().equals(registerCommand.getRepeatPassword());
    }
}