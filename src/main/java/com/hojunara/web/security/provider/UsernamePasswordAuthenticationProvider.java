package com.hojunara.web.security.provider;

import com.hojunara.web.security.authentication.UsernamePasswordAuthentication;
import com.hojunara.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Autowired
    public UsernamePasswordAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authenticate user info");

        String email = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        Boolean isAuthenticated = userService.authenticateUser(email, password);
        if (isAuthenticated) {
            return new UsernamePasswordAuthentication(email, password);
        }
        else {
            throw new AuthenticationException("Invalid email or password") {};
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }
}
