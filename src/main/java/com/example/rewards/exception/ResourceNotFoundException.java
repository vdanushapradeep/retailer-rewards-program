package com.example.rewards.exception;

/** Runtime exception used when a requested resource cannot be found. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
