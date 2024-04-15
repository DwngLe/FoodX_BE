package com.example.foodx_be.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User does not exits in our records");
    }
}
