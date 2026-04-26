package com.project.accesscontrol.domain.user.entity;

import com.project.accesscontrol.domain.role.entity.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    private static final long LOCK_DURATION_MINUTES = 5;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash" ,nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;

    @Column(name = "lock_until")

    private LocalDateTime lockUntil;

    @Column(name = "created_at", nullable = false, updatable = false,insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false,insertable = false)
    private LocalDateTime updated_at;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    protected User() {
    }

    public User(String username, String email, String passwordHash, boolean enabled) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean isCurrentlyLocked() {
        if (accountNonLocked) {
            return false;
        }
        if (lockUntil == null) {
            return true;
        }
        return lockUntil.isAfter(LocalDateTime.now());
    }

    public void registerFailedLoginAttempt() {
        this.failedLoginAttempts++;
        System.out.println("Failed login attempt " + this.failedLoginAttempts + " for user " + this.username);
        if (this.failedLoginAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
            this.accountNonLocked = false;
            this.lockUntil = LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES);
        }
    }

    public void registerSuccessfulLogin() {
        this.failedLoginAttempts = 0;
        this.accountNonLocked = true;
        this.lockUntil = null;
    }

    public void unlockIfLockExpired() {
        if (!accountNonLocked && lockUntil != null && lockUntil.isBefore(LocalDateTime.now())) {
            this.accountNonLocked = true;
            this.failedLoginAttempts = 0;
            this.lockUntil = null;
        }
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public LocalDateTime getLockUntil() {
        return lockUntil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updated_at;
    }

    public Set<Role> getRoles() {
        return roles;
    }


}
