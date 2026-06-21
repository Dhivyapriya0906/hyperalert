package com.hyperalert.hyperalert.controller;

import com.hyperalert.hyperalert.config.JwtUtil;
import com.hyperalert.hyperalert.dto.AuthRequest;
import com.hyperalert.hyperalert.dto.AuthResponse;
import com.hyperalert.hyperalert.entity.User;
import com.hyperalert.hyperalert.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        userService.registerUser(request.getUsername(), request.getPassword(), request.getEmail());
        String token = jwtUtil.generateToken(request.getUsername());
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        User user = userService.validateLogin(request.getUsername(), request.getPassword());
        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}