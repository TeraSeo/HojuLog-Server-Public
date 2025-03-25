package com.hojunara.web.exception;

public class BlogContentNotFoundException extends RuntimeException {
    public BlogContentNotFoundException(String message) {
        super(message);
    }
}
