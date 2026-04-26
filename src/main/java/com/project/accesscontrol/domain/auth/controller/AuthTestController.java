package com.project.accesscontrol.domain.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthTestController {

    @GetMapping("/api/me")
    public String me(Authentication authentication) {
        return "Authenticated as: " + authentication.getName();
    }
}