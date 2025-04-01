package com.hojunara.web.exception;

import org.springframework.security.authentication.LockedException;

public class UserLockedException extends LockedException {
    public UserLockedException(String message) {
        super(message);
    }
}
