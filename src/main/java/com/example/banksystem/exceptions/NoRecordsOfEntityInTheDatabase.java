package com.example.banksystem.exceptions;

public class NoRecordsOfEntityInTheDatabase extends RuntimeException{

    public NoRecordsOfEntityInTheDatabase(String message) {
        super("No records of " + message + " found .");
    }
}
