package com.project.accesscontrol.domain.user.dto;

import com.project.accesscontrol.domain.role.entity.Role;
import com.project.accesscontrol.domain.user.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                roles,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
