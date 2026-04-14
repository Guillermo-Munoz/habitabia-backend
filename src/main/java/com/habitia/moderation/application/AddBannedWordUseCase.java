package com.habitia.moderation.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.habitia.moderation.domain.BannedWord;
import com.habitia.moderation.domain.BannedWordRepository;
import com.habitia.shared.domain.exception.BusinessRuleException;

@Service
public class AddBannedWordUseCase {
    private final BannedWordRepository bannedWordRepository;

    public AddBannedWordUseCase(BannedWordRepository bannedWordRepository) {
        this.bannedWordRepository = bannedWordRepository;
    }
    public BannedWord execute(String word){
        if(bannedWordRepository.existsByWord(word)){
            throw new BusinessRuleException("word '" + word + "' is already banned");
        }
        return bannedWordRepository.save(new BannedWord(UUID.randomUUID(),word, LocalDateTime.now()));
    }
}
