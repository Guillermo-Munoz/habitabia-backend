package com.habitia.users.infrastructure.web;

import com.habitia.users.application.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase,
                          AuthenticateUserUseCase authenticateUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        var user = registerUserUseCase.execute(
                new RegisterUserCommand(
                        request.fullName(),
                        request.email(),
                        request.password(),
                        request.role()
                )
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserResponse(
                        user.getId().toString(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole().name()
                ));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        var result = authenticateUserUseCase.execute(
                request.email(),
                request.password()
        );
        return ResponseEntity.ok(
                new TokenResponse(result.token(), result.userId(), result.role())
        );
    }
}