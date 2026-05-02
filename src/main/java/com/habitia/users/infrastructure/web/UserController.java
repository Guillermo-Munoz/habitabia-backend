package com.habitia.users.infrastructure.web;

import com.habitia.users.application.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase,
                          AuthenticateUserUseCase authenticateUserUseCase,
                          GetCurrentUserUseCase getCurrentUserUseCase,
                          UpdateProfileUseCase updateProfileUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.updateProfileUseCase = updateProfileUseCase;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        var user = registerUserUseCase.execute(
                new RegisterUserCommand(
                        request.fullName(),
                        request.email(),
                        request.password()
                )
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserResponse(
                        user.getId().toString(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole().name(),
                        user.getBio(),
                        user.getAvatarUrl()
                ));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        var result = authenticateUserUseCase.execute(
                request.email(),
                request.password()
        );
        return ResponseEntity.ok(
                new TokenResponse(
                        result.token(),
                        result.userId(),
                        result.role()
                ));
    }

    @PatchMapping("/users/me")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication auth) {
        var user = updateProfileUseCase.execute(new UpdateProfileCommand(
                auth.getName(),
                request.fullName(),
                request.bio(),
                request.avatarUrl()
        ));
        return ResponseEntity.ok(new UserResponse(
                user.getId().toString(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getBio(),
                user.getAvatarUrl()
        ));
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserResponse> me(Authentication auth) {
        var user = getCurrentUserUseCase.execute(auth.getName());
        return ResponseEntity.ok(new UserResponse(
                user.getId().toString(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getBio(),
                user.getAvatarUrl()
        ));
    }
}
