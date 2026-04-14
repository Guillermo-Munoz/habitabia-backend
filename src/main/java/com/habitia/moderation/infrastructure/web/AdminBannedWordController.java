package com.habitia.moderation.infrastructure.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.habitia.moderation.application.AddBannedWordUseCase;
import com.habitia.moderation.application.DeleteBannedWordUseCase;
import com.habitia.moderation.application.GetAllBannedWordsUseCase;
import com.habitia.moderation.domain.BannedWord;

@RestController
@RequestMapping("/api/v1/admin/banned-words")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBannedWordController {
    private final AddBannedWordUseCase addBannedWord;
    private final DeleteBannedWordUseCase deleteBannedWord;
    private final GetAllBannedWordsUseCase getAllBannedWords;
    
    public AdminBannedWordController(AddBannedWordUseCase addBannedWord,
                                     DeleteBannedWordUseCase deleteBannedWord,
                                     GetAllBannedWordsUseCase getAllBannedWords){
            this.addBannedWord = addBannedWord;
            this.deleteBannedWord = deleteBannedWord;
            this.getAllBannedWords = getAllBannedWords;                            
    }
    @GetMapping
    public ResponseEntity<List<BannedWord>> getAll(){
        return ResponseEntity.ok(getAllBannedWords.execute());
    }
    @PostMapping
    public ResponseEntity<BannedWord> add(@RequestParam String word){
        return ResponseEntity.status(201).body(addBannedWord.execute(word));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        deleteBannedWord.execute(id);
        return ResponseEntity.noContent().build();
    }
}
