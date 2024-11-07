package com.promo.web.controller;

import com.promo.web.dto.UserDto;
import com.promo.web.entity.ApiResponse;
import com.promo.web.exception.UserAlreadyExistsException;
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

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("login")
    public ResponseEntity<Boolean> login() {
        return ResponseEntity.ok(true);
    }

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
}