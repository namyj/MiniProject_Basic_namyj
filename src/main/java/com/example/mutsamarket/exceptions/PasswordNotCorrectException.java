package com.example.mutsamarket.exceptions;

public class PasswordNotCorrectException extends Status404Exception{

    public PasswordNotCorrectException() {
        super("username or password does not correct");
    }
}
