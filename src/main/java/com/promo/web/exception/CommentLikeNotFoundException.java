package com.promo.web.exception;

public class CommentLikeNotFoundException extends RuntimeException {
    public CommentLikeNotFoundException(String message) {
        super(message);
    }
}