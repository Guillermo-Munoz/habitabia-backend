package com.habitia.moderation.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.habitia.moderation.domain.BannedWord;
import com.habitia.moderation.domain.BannedWordRepository;

@Component
public class BannedWordRepositoryAdapter implements BannedWordRepository {

    private final BannedWordJpaRepository jpaRepository;
    public BannedWordRepositoryAdapter(BannedWordJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    @Override
    public BannedWord save(BannedWord word) {
        return toDomain(jpaRepository.save(toEntity(word)));
    }
    @Override
    public void deleteById(UUID id){
        jpaRepository.deleteById(id);
    }
    @Override
    public Optional<BannedWord> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }
    @Override
    public List<BannedWord> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }
    @Override 
    public boolean existsByWord(String words){
        return jpaRepository.existsByWord(words.toLowerCase().trim());
    }
    private BannedWordJpaEntity toEntity(BannedWord word) {
        BannedWordJpaEntity e = new BannedWordJpaEntity();
        e.setId(word.getId());
        e.setWord(word.getWord());
        e.setCreatedAt(word.getCreatedAt());
        return e;
    }

    private BannedWord toDomain(BannedWordJpaEntity e) {
        return new BannedWord(e.getId(), e.getWord(), e.getCreatedAt());
    }
}
