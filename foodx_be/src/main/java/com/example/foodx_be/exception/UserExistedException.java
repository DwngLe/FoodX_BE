package com.example.foodx_be.exception;

public class UserExistedException extends RuntimeException{
    public UserExistedException(String username) {
        super("The username: '" + username + "' already exist");
    }
}
