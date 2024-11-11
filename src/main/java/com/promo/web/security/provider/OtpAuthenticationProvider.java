package com.promo.web.security.provider;

import com.promo.web.security.authentication.OtpAuthentication;
import com.promo.web.service.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private final OtpService otpService;

    @Autowired
    public OtpAuthenticationProvider(OtpService otpService) {
        this.otpService = otpService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Check otp code");

        String email = authentication.getName();
        String code = String.valueOf(authentication.getCredentials());

        Boolean isOtpCorrect = otpService.checkOtp(email, code);

        if (isOtpCorrect) {
            return new OtpAuthentication(email, code);
        }
        else {
            throw new AuthenticationException("Invalid otp code") {};
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
