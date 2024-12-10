package com.promo.web.exception;

public class CommentLikeNotFoundByCommentNUserException extends RuntimeException {
    public CommentLikeNotFoundByCommentNUserException(String message) {
        super(message);
    }
}