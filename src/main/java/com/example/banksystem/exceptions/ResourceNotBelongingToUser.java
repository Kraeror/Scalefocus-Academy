package com.example.banksystem.exceptions;

public class ResourceNotBelongingToUser extends RuntimeException{

    public ResourceNotBelongingToUser(String message) {
        super("You are not owner of this " + message);
    }
}
