package com.example.mutsamarket.exceptions;

public class UsernameNotFoundException extends Status404Exception {

    public UsernameNotFoundException() {
        super("target user not found");
    }
}
