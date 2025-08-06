package com.hojunara.web.controller;

import com.hojunara.web.dto.request.UserDto;
import com.hojunara.web.entity.ApiResponse;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.UserAlreadyExistsException;
import com.hojunara.web.security.provider.JwtTokenProvider;
import com.hojunara.web.service.OtpService;
import com.hojunara.web.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that handles user registration, OTP delivery,
 * and JWT token validation for access and refresh tokens.
 *
 * All endpoints are under the <code>/api/auth</code> path.
 *
 * @author Taejun Seo
 */
@RestController
@RequestMapping("api/auth")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(UserService userService, OtpService otpService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.otpService = otpService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registers a new user with the provided information.
     *
     * @param userDto the registration details
     * @return a success or failure response with appropriate message
     */
    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserDto userDto) {
        try {
            Boolean isCreated = userService.createUser(userDto);
            return ResponseEntity.ok(new ApiResponse(isCreated, isCreated ? "User created successfully" : "User creation failed"));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "User already exists"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Failed to register"));
        }
    }

    /**
     * Sends a one-time password (OTP) to the user's email.
     *
     * @param email the email to send the OTP to
     * @return {@code true} if the OTP was sent successfully, {@code false} otherwise
     */
    @PostMapping("send/otp")
    public ResponseEntity<Boolean> sendOtp(@RequestParam String email) {
        try {
            otpService.sendOtp(email);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Validates access and refresh tokens and returns authentication status.
     * <p>
     * If the access token is valid, sets the authentication context.
     * If only the refresh token is valid, generates a new access token.
     * Adds user ID and possibly new access token to the response headers.
     * </p>
     *
     * @param accessToken the access token (optional)
     * @param refreshToken the refresh token (optional)
     * @param response the HTTP response to set headers on
     * @return {@code true} if authenticated, {@code false} if user is locked (by admin), or error response
     */
    @PostMapping("validate/token")
    public ResponseEntity<?> validateToken(@RequestHeader(required = false) String accessToken,
                                           @RequestHeader(required = false) String refreshToken,
                                           HttpServletResponse response) {
        log.info("Validate JWT token");

        try {
            if (StringUtils.isBlank(accessToken) && StringUtils.isBlank(refreshToken)) {
                log.error("Both tokens are missing");
                return ResponseEntity.badRequest().body("Both tokens are missing");
            }

            // Validate access token
            if (StringUtils.isNotBlank(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String email = authentication.getName();
                User user = userService.getUserByEmail(email);

                log.info("is user locked: " + user.getIsLocked() + "!!!!!!!!!!!!!!!!!!!!!");
                if (user.getIsLocked()) return ResponseEntity.ok(false);

                log.info("userId: " + user.getId().toString());
                response.setHeader("userId", user.getId().toString());

                log.info("Access token is valid");
                return ResponseEntity.ok(true);
            }

            // Validate refresh token and regenerate access token
            if (StringUtils.isNotBlank(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
                String newAccessToken = jwtTokenProvider.regenerateAccessToken(refreshToken);
                Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String email = authentication.getName();
                User user = userService.getUserByEmail(email);

                log.info("is user locked: " + user.getIsLocked() + "!!!!!!!!!!!!!!!!!!!!!");
                if (user.getIsLocked()) return ResponseEntity.ok(false);

                response.setHeader("userId", user.getId().toString());

                log.info("Refresh token is valid, new access token generated");

                // Add the new access token to the response headers
                response.setHeader("accessToken", newAccessToken);
                return ResponseEntity.ok(true);
            }

            log.error("Both tokens are invalid");
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Both tokens are invalid");

        } catch (Exception e) {
            log.error("Error while validating tokens", e);
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("Token validation failed");
        }
    }
}