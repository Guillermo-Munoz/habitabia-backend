package com.habitia.moderation.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.habitia.moderation.domain.BannedWordRepository;
import com.habitia.shared.domain.exception.ResourceNotFoundException;

@Service
public class DeleteBannedWordUseCase {
    private final BannedWordRepository bannedWordRepository;

    public DeleteBannedWordUseCase(BannedWordRepository bannedWordRepository){
        this.bannedWordRepository = bannedWordRepository;
    }
    public void execute(UUID id) {
        bannedWordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BannedWord", id.toString()));
        bannedWordRepository.deleteById(id);
    }
}
