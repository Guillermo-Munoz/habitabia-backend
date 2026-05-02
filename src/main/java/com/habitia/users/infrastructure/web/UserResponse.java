package com.habitia.users.infrastructure.web;

public record UserResponse(String id, String fullName, String email, String role, String bio, String avatarUrl) {
}
