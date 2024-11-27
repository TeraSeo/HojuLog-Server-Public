package com.promo.web.security.filter;

import com.promo.web.security.provider.JwtTokenProvider;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Processing JwtAuthenticationFilter for path: {}", request.getServletPath());

        String accessToken = request.getHeader("accessToken");
        String refreshToken = request.getHeader("refreshToken");

        if (StringUtils.isBlank(accessToken) && StringUtils.isBlank(refreshToken)) {
            log.error("Missing tokens");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (StringUtils.isNotBlank(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Access token is valid");
            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.isNotBlank(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
            String newAccessToken = jwtTokenProvider.regenerateAccessToken(refreshToken);
            Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Refresh token is valid, new access token generated");

            response.setHeader("accessToken", newAccessToken);
            filterChain.doFilter(request, response);
            return;
        }

        log.error("Both tokens are invalid");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") || path.startsWith("/api/oauth/") || path.startsWith("/oauth");
    }
}
