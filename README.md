![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green)
![Docker](https://img.shields.io/badge/Config-Settings-blue)

# Access Control

A Spring Boot security lab that implements **both Session-Based Authentication and JWT-Based Authentication in the same backend**.

The goal of the project is not only “login works”, but to show how a backend can handle **authentication, authorization, token lifecycle, ownership rules, audit data, and security hardening** in a production-oriented way.

## What this project covers

### Session-Based Authentication
Built for traditional browser flows.

Includes:
- custom login page
- Spring Security form login
- session + cookie authentication
- protected dashboard
- admin-only web page
- role-based web authorization

This side models the classic:
browser -> login form -> session created -> cookie-based access

### JWT-Based Authentication
Built for stateless API flows.

Includes:
- JWT access token
- DB-backed refresh token
- refresh endpoint
- logout / revoke
- JWT authentication filter
- protected `/api/**` endpoints
- role-based API authorization

This side models the modern:
client -> token-based login -> bearer token requests -> refresh flow

## Key design decisions

### 1. Web and API security are separated
The project does not mix browser security and API security into one undefined flow.

- web side uses session/cookie behavior
- API side uses stateless bearer token behavior

This is one of the strongest architectural points of the project.

### 2. Role-based authorization exists in both worlds
Authorization is not limited to “user is logged in”.

Examples:
- admin-only web page
- admin-only API document listing
- authenticated user document access
- ownership-aware business rules

### 3. Ownership is enforced in backend logic
Document ownership is not trusted from request input.

The authenticated user becomes the owner through security context, which prevents clients from creating data on behalf of another user.

### 4. Audit is treated realistically
The project distinguishes between:

- DB-level audit: `created_at`, `updated_at`
- application-level audit: `created_by`, `updated_by`

This reflects real backend design:
time can be handled by DB,
but business-user identity must be handled by the application.

### 5. Refresh token lifecycle is controlled
JWT authentication is not limited to just generating access tokens.

The project includes:
- refresh token storage in DB
- refresh endpoint
- token rotation
- logout revoke support

This makes the JWT side far more realistic than a basic demo.

### 6. Login hardening exists
Authentication is protected with brute-force defense ideas such as:
- failed login attempt tracking
- temporary account locking
- lock expiration handling

This moves the project beyond simple happy-path authentication.

## Main domain

### User
Represents authenticated users of the system.

### Role
Used to control both web and API access.
Examples:
- USER
- ADMIN

### Document
Represents user-owned business data.

### RefreshToken
Represents refresh token state for JWT authentication.

It demonstrates:
- two authentication models in one codebase
- web vs API security separation
- role-based authorization
- ownership-based authorization
- audit-aware business logic
- refresh token lifecycle
- brute-force protection
- production-oriented security thinking

## Tech Stack
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- Thymeleaf
- JWT

## Summary
This project is a compact but production-aware backend security.

It shows how to build and compare:

- Session-Based Authentication
- JWT-Based Authentication

while also covering the pieces that make real systems valuable:
- authorization
- ownership
- audit fields
- refresh token management
- revoke/logout behavior
- login hardening

It is built to reflect backend architecture and security decisions, not just endpoint implementation.
