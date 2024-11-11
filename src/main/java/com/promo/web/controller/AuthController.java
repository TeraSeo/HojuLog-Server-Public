package com.promo.web.controller;

import com.promo.web.dto.UserDto;
import com.promo.web.entity.ApiResponse;
import com.promo.web.exception.UserAlreadyExistsException;
import com.promo.web.service.OtpService;
import com.promo.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;

    @Autowired
    public AuthController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

//    @CrossOrigin(origins = "http://localhost:3000")
//    @PostMapping("login")
//    public ResponseEntity<ApiResponse> login(@RequestHeader("email") String email, @RequestHeader("password") String password) {
//        log.info("login");
//        try {
//            boolean isAuthenticated = userService.authenticateUser(email, password);
//            if (isAuthenticated) {
//                return ResponseEntity.ok(new ApiResponse(true, "Login successful"));
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(new ApiResponse(false, "Invalid credentials"));
//            }
//        } catch (Exception e) {
//            log.error("Login failed for user: {}", email, e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse(false, "Login failed due to server error"));
//        }
//    }

    @CrossOrigin(origins = "http://localhost:3000")
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

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("send/otp")
    public ResponseEntity<Boolean> sendOtp(@RequestParam String email) {
        try {
            otpService.sendOtp(email);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

//    @CrossOrigin(origins = "http://localhost:3000")
//    @PostMapping("verify/otp")
//    public ResponseEntity<Boolean> checkOtp(@RequestParam String email, @RequestParam String code) {
//        try {
//            Boolean isOtpCorrect = otpService.checkOtp(email, code);
//            return ResponseEntity.ok(isOtpCorrect);
//        } catch (Exception e) {
//            return ResponseEntity.ok(false);
//        }
//    }
}