package com.project.accesscontrol.domain.user.controller;

import com.project.accesscontrol.domain.user.dto.RegisterUserRequest;
import com.project.accesscontrol.domain.user.dto.UserMapper;
import com.project.accesscontrol.domain.user.dto.UserResponse;
import com.project.accesscontrol.domain.user.entity.User;
import com.project.accesscontrol.domain.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterUserRequest request) {
        User user = userService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        return UserMapper.toResponse(user);
    }

    @GetMapping("/{username}")
    public UserResponse getByUsername(@PathVariable String username) {
        User user = userService.getRequiredUserWithRolesByUsername(username);
        return UserMapper.toResponse(user);
    }
}
