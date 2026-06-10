package com.tanisha.bfhl.exception;

/**
 * Thrown when the incoming request payload is invalid or malformed.
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
