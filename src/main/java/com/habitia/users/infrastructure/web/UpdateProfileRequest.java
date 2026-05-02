package com.habitia.users.infrastructure.web;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank String fullName,
        String bio,
        String avatarUrl
) {}
