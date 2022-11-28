package com.example.banksystem.exceptions;

/**Constructs an <code>AccountNotFoundException</code> with a custom message.
 * <p>This exception occurs when an entity can't be found with the given ID or Iban.
 */
public class EntityNotFoundException extends RuntimeException{
    /**
     * Constructs a new AccountNotFound exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public EntityNotFoundException(String message) {
        super("This " +  message + " doesn't exists!");
    }
}
