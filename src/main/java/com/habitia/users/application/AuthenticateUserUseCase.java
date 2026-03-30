package com.habitia.users.application;

import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.shared.infrastructure.security.JwtTokenProvider;
import com.habitia.users.domain.User;
import com.habitia.users.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserUseCase {

    private static final Logger log = LoggerFactory.getLogger(AuthenticateUserUseCase.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticateUserUseCase(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        log.info("✅ AuthenticateUserUseCase inicializado");
    }

    public AuthTokenResult execute(String email, String password) {
        log.debug("Intento de autenticación para email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado: {}", email);
                    return new BusinessRuleException("Invalid credentials");
                });

         if (!user.passwordMatches(password, passwordEncoder)) {
            log.warn("Contraseña incorrecta para usuario: {}", email);
            throw new BusinessRuleException("Invalid credentials");
        }

        if (!user.isActive()) {
            log.warn("Usuario inactivo: {}", email);
            throw new BusinessRuleException("User account is disabled");
        }

        String token = jwtTokenProvider.generateToken(
                user.getId().value(),
                user.getRole().name()
        );

        log.info("Usuario autenticado exitosamente: {}", email);

        return new AuthTokenResult(token, user.getId().toString(), user.getRole().name());
    }
}