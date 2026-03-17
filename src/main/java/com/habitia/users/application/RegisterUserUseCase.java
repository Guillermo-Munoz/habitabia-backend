package com.habitia.users.application;

import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.users.domain.User;
import com.habitia.users.domain.UserRepository;
import com.habitia.users.domain.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new BusinessRuleException("Email already regidtered: " + command.email());
        }

        String hashedPassword = passwordEncoder.encode(command.password());

        User user = new User(
                command.fullName(),
                command.email(),
                hashedPassword,
                UserRole.valueOf(command.role())
        );
        return userRepository.save(user);
    }
}