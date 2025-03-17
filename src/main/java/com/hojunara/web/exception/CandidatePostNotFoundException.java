package com.hojunara.web.exception;

public class CandidatePostNotFoundException extends RuntimeException {
    public CandidatePostNotFoundException(String message) {
        super(message);
    }
}