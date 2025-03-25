package com.hojunara.web.exception;

public class ArticlePostNotFoundException extends RuntimeException {
    public ArticlePostNotFoundException(String message) {
        super(message);
    }
}
