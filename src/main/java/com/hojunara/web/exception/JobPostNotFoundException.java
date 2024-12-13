package com.hojunara.web.exception;

public class JobPostNotFoundException extends RuntimeException {
    public JobPostNotFoundException(String message) {
        super(message);
    }
}