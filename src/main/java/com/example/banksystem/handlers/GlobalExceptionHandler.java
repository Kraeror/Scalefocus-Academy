package com.example.banksystem.handlers;

import com.example.banksystem.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LogManager.getLogger(this.getClass());

    /**
     * An {@link ExceptionHandler} for {@link EntityNotFoundException}.
     * <p>This exception occurs when an account can't be found with the given ID or Iban.
     * @param exception the exception that was thrown
     * @return a {@link ResponseEntity<String>} with the exception message and a HTTP code of Bad Request.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exception)
    {
        log.warn("Caught this entity not existing : ",exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * An {@link ExceptionHandler} for {@link InsufficientFundsException}.
     * <p>This exception occurs when you try to withdraw or transfer more money than you have in the selected account.
     * @param exception the exception that was thrown
     * @return a {@link ResponseEntity<String>} with the exception message and a HTTP code of Bad Request.
     */
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException exception)
    {
        log.warn("Caught attempt to do a transaction for which the account has insufficient funds. ",exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * An {@link ExceptionHandler} for {@link PasswordNotMatchingException}.
     * <p>This exception occurs when a password input does not match the original.
     * @param exception the exception that was thrown
     * @return a {@link ResponseEntity<String>} with the exception message and a HTTP code of Bad Request.
     */
    @ExceptionHandler(PasswordNotMatchingException.class)
    public ResponseEntity<String> handlePasswordNotMatchingException(PasswordNotMatchingException exception)
    {
        log.warn("Caught password mismatch. ",exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotBelongToUserException.class)
    public ResponseEntity<String> handleAccountNotBelongToUserException(AccountNotBelongToUserException exception)
    {
        log.warn("Caught user's account security exception.", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IncorrectDateFilteringException.class)
    public ResponseEntity<String> handleIncorrectDateFilteringException(IncorrectDateFilteringException exception)
    {
        log.warn("Caught data filtering exception.", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.warn("Caught exception: " + exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("Caught exception: " + exception);
        return new ResponseEntity<>("You have an error in field " + exception.getFieldErrors().get(0).getField(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException exception) {
        log.warn("Caught exception: " + exception);
        return new ResponseEntity<>("The object is not present at all.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException exception) {
        log.warn("Caught exception: " + exception);
        return new ResponseEntity<>("Invalid password.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        log.warn("Caught exception: " + exception);
        return new ResponseEntity<>("Invalid username.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoRecordsOfEntityInTheDatabase.class)
    public ResponseEntity<String> handleUsernameNotFoundException(NoRecordsOfEntityInTheDatabase exception) {
        log.warn("Caught exception: " + exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormatEx(NumberFormatException exception) {
        log.warn("Caught exception: " + exception);
        return new ResponseEntity<>("Please enter number instead of string.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotBelongingToUser.class)
    public ResponseEntity<String> handleResourceNotBelongingToUser(ResourceNotBelongingToUser exception) {
        log.warn("Caught exception: " + exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
