package com.hojunara.web.exception;

public class TravelPostNotFoundException extends RuntimeException {
    public TravelPostNotFoundException(String message) {
        super(message);
    }
}