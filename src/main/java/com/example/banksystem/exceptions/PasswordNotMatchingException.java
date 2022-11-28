package com.example.banksystem.exceptions;

/**
 * Constructs a <code>PasswordNotMatchingException</code> with a custom message.
 * <p>This exception occurs when a password input does not match the original.
 */
public class PasswordNotMatchingException extends RuntimeException{
    /**
     * Constructs a new PasswordNotMatching exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public PasswordNotMatchingException(String message) {
        super(message);
    }
}
