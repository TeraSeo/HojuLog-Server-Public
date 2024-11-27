package com.promo.web.security.filter;

import com.promo.web.entity.JwtToken;
import com.promo.web.entity.User;
import com.promo.web.repository.UserRepository;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class InitialAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager manager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public InitialAuthenticationFilter(AuthenticationManager manager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.manager = manager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
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
                Optional<User> u = userRepository.findByEmail(email);
                if (u.isEmpty()) return;

                Authentication a = new OtpAuthentication(email, code, List.of(new SimpleGrantedAuthority(u.get().getRole().toString())));
                manager.authenticate(a);

                JwtToken token = jwtTokenProvider.generateToken(a);
                String accessToken = token.getAccessToken();
                String refreshToken = token.getRefreshToken();

                response.setHeader("accessToken", accessToken);
                response.setHeader("refreshToken", refreshToken);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(request.getServletPath().equals("/api/auth/login") || request.getServletPath().equals("/api/auth/verify/otp"));
    }
}