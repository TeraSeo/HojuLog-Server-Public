package com.hojunara.web.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class UserAlreadyExistsException extends OAuth2AuthenticationException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}