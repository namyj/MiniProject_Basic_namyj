package com.example.mutsamarket.exceptions;

public class PasswordNotCorrectException extends Status404Exception{

    public PasswordNotCorrectException() {
        super("password was not correct");
    }
}
