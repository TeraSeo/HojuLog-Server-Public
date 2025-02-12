package com.hojunara.web.exception;

public class KeywordNotFoundException extends RuntimeException {
    public KeywordNotFoundException(String message) {
        super(message);
    }
}