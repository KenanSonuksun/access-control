package com.project.accesscontrol.domain.auth.service;

import com.project.accesscontrol.common.exception.ResourceNotFoundException;
import com.project.accesscontrol.domain.auth.dto.LoginResponse;
import com.project.accesscontrol.domain.auth.entity.RefreshToken;
import com.project.accesscontrol.domain.auth.repository.RefreshTokenRepository;
import com.project.accesscontrol.domain.user.entity.User;
import com.project.accesscontrol.domain.user.service.UserSecurityService;
import com.project.accesscontrol.domain.user.service.UserService;
import com.project.accesscontrol.security.jwt.JwtTokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserSecurityService userSecurityService;

    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder,
                       JwtTokenService jwtTokenService,
                       RefreshTokenRepository refreshTokenRepository,
                       UserSecurityService userSecurityService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userSecurityService = userSecurityService;
    }

    public LoginResponse login(String username, String rawPassword) {
        User user = userService.getRequiredUserWithRolesByUsername(username);

        if (!user.isAccountNonLocked()) {
            userSecurityService.unlockIfExpired(user.getId());
            user = userService.getRequiredUserWithRolesByUsername(username);
        }

        if (user.isCurrentlyLocked()) {
            throw new com.project.accesscontrol.common.exception.AccountLockedException(
                    "Account is temporarily locked. Please try again later."
            );
        }

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            userSecurityService.recordFailedLoginAttempt(user.getId());
            throw new BadCredentialsException("Invalid username or password");
        }
        userSecurityService.recordSuccessfulLogin(user.getId());

        return issueTokensForUser(user);
    }

    public LoginResponse refresh(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

        if (refreshToken.isRevoked()) {
            throw new BadCredentialsException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Refresh token has expired");
        }

        User user = refreshToken.getUser();
        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);
        return issueTokensForUser(user);
    }

    public void logout(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
        if (!refreshToken.isRevoked()) {
            refreshToken.revoke();
            refreshTokenRepository.save(refreshToken);
        }
    }

    private LoginResponse issueTokensForUser(User user) {
        String accessToken = jwtTokenService.generateAccessToken(user.getUsername());

        String refreshTokenValue = jwtTokenService.generateRefreshTokenValue();
        LocalDateTime refreshTokenExpiresAt = jwtTokenService.getRefreshTokenExpiresAt();

        RefreshToken refreshToken = new RefreshToken(
                refreshTokenValue,
                user,
                refreshTokenExpiresAt,
                false
        );

        refreshTokenRepository.save(refreshToken);

        return new LoginResponse(
                accessToken,
                refreshTokenValue,
                "Bearer",
                jwtTokenService.getAccessTokenExpiresAt(),
                refreshTokenExpiresAt
        );
    }
}