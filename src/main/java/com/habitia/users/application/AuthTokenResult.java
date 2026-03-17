package com.habitia.users.application;

public record AuthTokenResult(String token, String userId, String role) {
}
