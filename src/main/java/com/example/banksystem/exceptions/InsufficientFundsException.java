package com.example.banksystem.exceptions;

/**Constructs an <code>InsufficientFundsException</code> with a custom message.
 * <p>This exception occurs when you try to withdraw or transfer more money than you have in the selected account.
 *
 */
public class InsufficientFundsException extends RuntimeException{
    /**
     * Constructs a new InsufficientFunds exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InsufficientFundsException(String message) {
        super(message);
    }
}
