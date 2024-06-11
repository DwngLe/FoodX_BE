package com.example.foodx_be.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // Kiểm tra độ dài từ 5-8 ký tự
        if (value == null || value.length() < 5 || value.length() > 10) {
            return false;
        }

        // Kiểm tra chỉ chứa các ký tự từ a-z và số từ 0-9
        if (!value.matches("^[a-z0-9]*$")) {
            return false;
        }

        // Kiểm tra không có ký tự viết hoa
        if (!value.equals(value.toLowerCase())) {
            return false;
        }

        return true;
    }
}
