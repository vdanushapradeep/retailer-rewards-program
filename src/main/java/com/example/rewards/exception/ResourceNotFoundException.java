package com.example.rewards.exception;

/**
 * Runtime exception used when a requested resource cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Create the exception with a human-readable message.
     *
     * @param message problem detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
