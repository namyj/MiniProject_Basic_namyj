package com.example.mutsamarket.exceptions;

public class OfferNotFoundException extends Status404Exception {

    public OfferNotFoundException() {
        super("target offer not found");
    }
}
