package com.promo.web.security.filter;

import com.promo.web.entity.JwtToken;
import com.promo.web.security.authentication.OtpAuthentication;
import com.promo.web.security.authentication.UsernamePasswordAuthentication;
import com.promo.web.security.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class InitialAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager manager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public InitialAuthenticationFilter(AuthenticationManager manager, JwtTokenProvider jwtTokenProvider) {
        this.manager = manager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Initial filter init");
        String method = request.getHeader("method");
        String email = request.getHeader("email");
        String password = request.getHeader("password");
        String code = request.getHeader("code");

        if ("otp".equals(method)) {
            if (!StringUtils.hasText(code)) {
                Authentication a = new UsernamePasswordAuthentication(email, password);
                manager.authenticate(a);
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
            }
            else {
                Authentication a = new OtpAuthentication(email, code);
                manager.authenticate(a);

                JwtToken token = jwtTokenProvider.generateToken(a);
                String accessToken = token.getAccessToken();
                String refreshToken = token.getRefreshToken();

                response.setHeader("AccessToken", accessToken);
                response.setHeader("RefreshToken", refreshToken);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(request.getServletPath().equals("/api/auth/login") || request.getServletPath().equals("/api/auth/verify/otp"));
    }
}