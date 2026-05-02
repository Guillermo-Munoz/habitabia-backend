package com.habitia.users.application;

import com.habitia.shared.domain.exception.ResourceNotFoundException;
import com.habitia.shared.domain.valueobject.UserId;
import com.habitia.users.domain.User;
import com.habitia.users.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateProfileUseCase {

    private final UserRepository userRepository;

    public UpdateProfileUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UpdateProfileCommand command) {
        User user = userRepository.findById(new UserId(UUID.fromString(command.userId())))
                .orElseThrow(() -> new ResourceNotFoundException("User", command.userId()));

        user.updateProfile(command.fullName(), command.bio(), command.avatarUrl());

        return userRepository.save(user);
    }
}
