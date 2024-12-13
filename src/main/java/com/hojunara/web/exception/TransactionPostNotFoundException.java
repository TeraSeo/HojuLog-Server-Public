package com.hojunara.web.exception;

public class TransactionPostNotFoundException extends RuntimeException {
    public TransactionPostNotFoundException(String message) {
        super(message);
    }
}