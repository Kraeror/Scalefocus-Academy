package com.example.banksystem.exceptions;

public class IncorrectDateFilteringException extends RuntimeException{
    public IncorrectDateFilteringException() {
        super("You cannot filter with the combination of date on, date before, and date after!");
    }
}
