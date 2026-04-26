package com.project.accesscontrol.domain.user.service;

import com.project.accesscontrol.common.exception.ResourceNotFoundException;
import com.project.accesscontrol.domain.user.entity.User;
import com.project.accesscontrol.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSecurityService {

    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailedLoginAttempt(Long userId) {
        User user = getUser(userId);
        user.registerFailedLoginAttempt();
        userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordSuccessfulLogin(Long userId) {
        User user = getUser(userId);
        user.registerSuccessfulLogin();
        userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void unlockIfExpired(Long userId) {
        User user = getUser(userId);
        user.unlockIfLockExpired();
        userRepository.save(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}