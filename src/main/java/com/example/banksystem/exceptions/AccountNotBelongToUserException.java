package com.example.banksystem.exceptions;

public class AccountNotBelongToUserException extends RuntimeException{
    public AccountNotBelongToUserException() {
        super("The account doesn't belong to this user");
    }
}
