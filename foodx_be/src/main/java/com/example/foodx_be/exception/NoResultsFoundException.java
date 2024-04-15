package com.example.foodx_be.exception;

public class NoResultsFoundException extends RuntimeException{
    public NoResultsFoundException() {
        super("No results found");
    }
}
