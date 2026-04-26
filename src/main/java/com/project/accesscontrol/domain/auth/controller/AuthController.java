package com.project.accesscontrol.domain.auth.controller;

import com.project.accesscontrol.domain.auth.dto.LoginRequest;
import com.project.accesscontrol.domain.auth.dto.LoginResponse;
import com.project.accesscontrol.domain.auth.dto.LogoutRequest;
import com.project.accesscontrol.domain.auth.dto.RefreshTokenRequest;
import com.project.accesscontrol.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request.getUsername(), request.getPassword());
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request.getRefreshToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();

    }
}