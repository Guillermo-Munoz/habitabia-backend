package com.habitia.shared.domain.moderation;

import com.habitia.moderation.domain.BannedWord;
import com.habitia.moderation.domain.BannedWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentModerationServiceTest {

    @Mock
    private BannedWordRepository bannedWordRepository;

    private ContentModerationService moderationService;

    @BeforeEach
    void setUp() {
        // Setup mínimo — sin datos de negocio. Cada test configura lo que necesita.
        lenient().when(bannedWordRepository.findAll()).thenReturn(List.of());
        moderationService = new ContentModerationService(bannedWordRepository);
    }

    // -------------------------------------------------------------------------
    // Input validation
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Input validation")
    class InputValidation {

        @Test
        @DisplayName("should pass when text is null")
        void shouldPass_whenTextIsNull() {
            assertTrue(moderationService.analyze(null).passed());
        }

        @Test
        @DisplayName("should pass when text is blank")
        void shouldPass_whenTextIsBlank() {
            assertTrue(moderationService.analyze("   ").passed());
        }

        @Test
        @DisplayName("should reject when text exceeds 1000 characters")
        void shouldReject_whenTextExceedsMaxLength() {
            assertFalse(moderationService.analyze("a".repeat(1001)).passed());
        }

        @Test
        @DisplayName("should pass when text is exactly 1000 characters")
        void shouldPass_whenTextIsExactlyMaxLength() {
            assertTrue(moderationService.analyze("a".repeat(1000)).passed());
        }
    }

    // -------------------------------------------------------------------------
    // Content filtering
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Content filtering")
    class ContentFiltering {

        @Test
        @DisplayName("should reject when text contains a banned word")
        void shouldReject_whenTextContainsBannedWord() {
            when(bannedWordRepository.findAll()).thenReturn(List.of(
                    new BannedWord(UUID.randomUUID(), "badword", LocalDateTime.now())
            ));
            moderationService = new ContentModerationService(bannedWordRepository);

            assertFalse(moderationService.analyze("this is a badword text").passed());
        }

        @Test
        @DisplayName("should pass when text has no banned words")
        void shouldPass_whenTextHasNoBannedWords() {
            assertTrue(moderationService.analyze("Great place, very clean!").passed());
        }

        @ParameterizedTest
        @ValueSource(strings = {"s3x", "p0rn", "f*ck", "sh1t", "b1tch"})
        @DisplayName("should reject evasion patterns")
        void shouldReject_evasionPatterns(String text) {
            assertFalse(moderationService.analyze(text).passed());
        }
    }

    // -------------------------------------------------------------------------
    // Tone analysis
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Tone analysis")
    class ToneAnalysis {

        @Test
        @DisplayName("should reject when text is excessively uppercase")
        void shouldReject_whenTextIsExcessivelyUppercase() {
            assertFalse(moderationService.analyze("THIS IS A VERY ANGRY REVIEW").passed());
        }

        @Test
        @DisplayName("should pass when text has normal casing")
        void shouldPass_whenTextHasNormalCasing() {
            assertTrue(moderationService.analyze("This place was really nice.").passed());
        }
    }

    // -------------------------------------------------------------------------
    // Spam detection
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Spam detection")
    class SpamDetection {

        @Test
        @DisplayName("should reject when text has five or more consecutive exclamation marks")
        void shouldReject_whenTextHasExcessiveExclamationMarks() {
            assertFalse(moderationService.analyze("Amazing!!!!!").passed());
        }

        @Test
        @DisplayName("should pass when text has fewer than five exclamation marks")
        void shouldPass_whenTextHasFewExclamationMarks() {
            assertTrue(moderationService.analyze("Amazing!").passed());
        }
    }

    // -------------------------------------------------------------------------
    // Rule composition
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Rule composition")
    class RuleComposition {

        @Test
        @DisplayName("should reject on first failing rule even if others would pass")
        void shouldReject_onFirstFailingRule() {
            when(bannedWordRepository.findAll()).thenReturn(List.of(
                    new BannedWord(UUID.randomUUID(), "clean", LocalDateTime.now())
            ));
            moderationService = new ContentModerationService(bannedWordRepository);

            ModerationResult result = moderationService.analyze("clean place");
            assertFalse(result.passed());
            assertEquals("Contains inappropriate language", result.reason());
        }

        @Test
        @DisplayName("should pass only when all rules are satisfied")
        void shouldPass_whenAllRulesAreSatisfied() {
            assertTrue(moderationService.analyze("Great stay, would recommend!").passed());
        }
    }
}
