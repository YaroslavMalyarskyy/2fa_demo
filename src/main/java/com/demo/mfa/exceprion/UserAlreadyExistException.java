package com.demo.mfa.exceprion;

/**
 * UserAlreadyExistException
 */
public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(final String message) {
        super(message);
    }
}