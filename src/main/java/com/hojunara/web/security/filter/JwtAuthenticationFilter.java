package com.hojunara.web.security.filter;

import com.hojunara.web.entity.User;
import com.hojunara.web.repository.UserRepository;
import com.hojunara.web.security.provider.JwtTokenProvider;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getServletPath();

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

            String email = authentication.getName();
            Optional<User> u = userRepository.findByEmail(email);
            if (u.isPresent()) {
                User user = u.get();
                if (path.contains("own")) {
                    String userId = request.getHeader("userId");
                    if (!user.getId().toString().equals(userId)) {
                        return;
                    }
                }
                response.setHeader("userId", user.getId().toString());
            }

            log.info("Access token is valid");

            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.isNotBlank(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
            String newAccessToken = jwtTokenProvider.regenerateAccessToken(refreshToken);
            Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String email = authentication.getName();
            Optional<User> u = userRepository.findByEmail(email);
            if (u.isPresent()) {
                User user = u.get();
                if (path.contains("own")) {
                    String userId = request.getHeader("userId");
                    if (!user.getId().toString().equals(userId)) {
                        return;
                    }
                }
                response.setHeader("userId", user.getId().toString());
            }

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
        return path.startsWith("/api/auth/") || path.startsWith("/api/oauth/") || path.startsWith("/oauth") || path.startsWith("/api/post/get") || path.startsWith("/api/user/get") || path.startsWith("/api/comment/get");
    }
}
