package com.example.mutsamarket.exceptions;

public class ItemNotFoundException extends Status404Exception {

    public ItemNotFoundException() {
        super("target item not found");
    }
}
