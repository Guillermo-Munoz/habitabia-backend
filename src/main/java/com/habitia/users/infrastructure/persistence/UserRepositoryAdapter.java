package com.habitia.users.infrastructure.persistence;

import com.habitia.shared.domain.valueobject.UserId;
import com.habitia.users.domain.User;
import com.habitia.users.domain.UserMapper;
import com.habitia.users.domain.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository, UserMapper userMapper) {
        this.jpaRepository = jpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        // Guardar sin cambiar la contraseña
        UserJpaEntity entity = userMapper.toEntity(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public User save(User user, String newHashedPassword) {
        // Guardar con nueva contraseña (para cambios de password)
        UserJpaEntity entity = userMapper.toEntity(user, newHashedPassword);
        UserJpaEntity saved = jpaRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.value())
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}