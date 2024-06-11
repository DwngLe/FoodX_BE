package com.example.foodx_be.validation;

import com.example.foodx_be.dto.request.UserCreationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserCreationRequest userCreationRequest = (UserCreationRequest) obj;
        return userCreationRequest.getPassword().equals(userCreationRequest.getRepeatPassword());
    }
}
