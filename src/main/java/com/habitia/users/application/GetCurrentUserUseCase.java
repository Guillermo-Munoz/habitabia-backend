package com.habitia.users.application;

import com.habitia.shared.domain.exception.ResourceNotFoundException;
import com.habitia.shared.domain.valueobject.UserId;
import com.habitia.users.domain.User;
import com.habitia.users.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetCurrentUserUseCase {

    private final UserRepository userRepository;

    public GetCurrentUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String userId) {
        return userRepository.findById(new UserId(UUID.fromString(userId)))
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
