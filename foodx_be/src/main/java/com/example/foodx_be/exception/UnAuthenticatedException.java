package com.example.foodx_be.exception;

public class UnAuthenticatedException extends RuntimeException {
    public UnAuthenticatedException() {
        super("Authentication failed");
    }
}
