package com.habitia.moderation.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.habitia.moderation.domain.BannedWord;
import com.habitia.moderation.domain.BannedWordRepository;

@Service
public class GetAllBannedWordsUseCase {
    private final BannedWordRepository bannedWordRepository;
    
    public GetAllBannedWordsUseCase(BannedWordRepository bannedWordRepository){
        this.bannedWordRepository = bannedWordRepository;
    }
    public List<BannedWord> execute(){
        return bannedWordRepository.findAll();
    }
}
