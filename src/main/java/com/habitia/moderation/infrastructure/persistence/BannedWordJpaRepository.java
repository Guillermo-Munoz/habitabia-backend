package com.habitia.moderation.infrastructure.persistence;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannedWordJpaRepository extends JpaRepository<BannedWordJpaEntity, UUID> {
    boolean existsByWord(String word);
}
