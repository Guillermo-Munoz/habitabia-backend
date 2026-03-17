package com.habitia.users.infrastructure.web;

public record TokenResponse(String token, String userId, String role) {
}
