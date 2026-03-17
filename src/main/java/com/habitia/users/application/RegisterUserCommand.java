package com.habitia.users.application;

public record RegisterUserCommand(
        String fullName,
        String email,
        String password,
        String role
) { }
