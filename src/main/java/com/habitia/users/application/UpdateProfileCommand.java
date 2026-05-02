package com.habitia.users.application;

public record UpdateProfileCommand(
        String userId,
        String fullName,
        String bio,
        String avatarUrl
) {}
