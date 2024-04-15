package com.example.foodx_be.exception;

public class PasswordMissMatchException extends  RuntimeException{
    public PasswordMissMatchException() {
        super("Password and repeat password do not match");
    }
}
