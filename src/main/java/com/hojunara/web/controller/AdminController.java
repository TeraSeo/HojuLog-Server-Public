package com.hojunara.web.controller;

import com.hojunara.web.entity.Role;
import com.hojunara.web.entity.User;
import com.hojunara.web.security.provider.JwtTokenProvider;
import com.hojunara.web.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
@Slf4j
public class AdminController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AdminController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("validate/token")
    public ResponseEntity<?> validateToken(@RequestHeader(required = false) String accessToken,
                                           @RequestHeader(required = false) String refreshToken) {
        log.info("Validate JWT token");

        try {
            if (StringUtils.isBlank(accessToken) && StringUtils.isBlank(refreshToken)) {
                log.error("Both tokens are missing");
                return ResponseEntity.badRequest().body("Tokens are needed");
            }

            if (StringUtils.isNotBlank(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String email = authentication.getName();
                User user = userService.getUserByEmail(email);
                if (user.getRole().equals(Role.ADMIN)) {
                    return ResponseEntity.ok(true);
                }
                else {
                    return ResponseEntity.ok(false);
                }
            }

            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Token is not valid");

        } catch (Exception e) {
            log.error("Error while validating tokens", e);
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("Token validation failed");
        }
    }
}
