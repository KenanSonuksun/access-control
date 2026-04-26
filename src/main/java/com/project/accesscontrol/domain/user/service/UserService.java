package com.project.accesscontrol.domain.user.service;

import com.project.accesscontrol.common.exception.ConflictException;
import com.project.accesscontrol.common.exception.ResourceNotFoundException;
import com.project.accesscontrol.domain.role.entity.Role;
import com.project.accesscontrol.domain.role.service.RoleService;
import com.project.accesscontrol.domain.user.entity.User;
import com.project.accesscontrol.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,RoleService roleService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User registerUser(String username, String email, String rawPassword) {

        validateUsernameAndEmailUniqueness(username, email);

        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User(username, email, encodedPassword, true);

        Role userRole = roleService.getRequiredRole("USER");

        user.addRole(userRole);

        return userRepository.save(user);

    }

    @Transactional(readOnly = true)
    public User getRequiredUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Transactional(readOnly = true)
    public User getRequiredUserWithRolesByUsername(String username) {
        return userRepository.findWithRolesByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Transactional(readOnly = true)
    public User getRequiredUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private void validateUsernameAndEmailUniqueness(String username, String email){

        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("Username already exists: " + username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already exists: " + email);
        }
    }


}
