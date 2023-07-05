package com.example.mutsamarket.exceptions;

public class CommentNotFoundException extends Status404Exception {

    public CommentNotFoundException() {
        super("target comment not found");
    }
}
