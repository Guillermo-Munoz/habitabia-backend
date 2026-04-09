package com.habitia.moderation.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BannedWordRepository {
    BannedWord save(BannedWord Word);
    void deleteById(UUID id);
    Optional<BannedWord> findById(UUID id);
    List<BannedWord> findAll();
    boolean existsByWord(String word);

}
