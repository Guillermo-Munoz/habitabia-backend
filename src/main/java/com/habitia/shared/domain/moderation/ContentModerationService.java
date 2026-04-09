package com.habitia.shared.domain.moderation;

import com.habitia.moderation.domain.BannedWordRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio de moderación de contenido para reseñas y otros textos públicos.
 *
 * Aplica cuatro capas de análisis en orden de coste computacional:
 *   1. Palabras prohibidas (BD con caché)
 *   2. Patrones de evasión (homoglifos, sustituciones)
 *   3. Agresividad por exceso de mayúsculas
 *   4. Spam por caracteres especiales repetidos
 *
 * Si el texto supera cualquier capa → ModerationResult.rejected()
 * Si pasa todas → ModerationResult.ok() → la reseña se aprueba automáticamente.
 *
 * Thread-safety: la caché usa volatile + bloque sincronizado para evitar
 * condiciones de carrera en entornos concurrentes.
 */
@Service
public class ContentModerationService {

    private final BannedWordRepository bannedWordRepository;

    // ==============================
    // CONFIGURACIÓN
    // ==============================

    private static final Duration  CACHE_TTL                  = Duration.ofMinutes(10);
    private static final int       MAX_TEXT_LENGTH             = 1000;
    private static final int       MIN_LETTERS_FOR_UPPERCASE   = 10;
    private static final double    UPPERCASE_THRESHOLD         = 0.7;

    // Precompilado una sola vez — compilar Pattern en cada llamada es costoso
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!?]{5,}");

    /**
     * Patrones que detectan evasiones comunes: s3x, p*rn, f.u.c.k, etc.
     * Usar CASE_INSENSITIVE evita tener que normalizar antes de aplicarlos.
     */
    private static final List<Pattern> EVASION_PATTERNS = List.of(
            Pattern.compile("s[3e€]x",         Pattern.CASE_INSENSITIVE),
            Pattern.compile("p[0o]rn",          Pattern.CASE_INSENSITIVE),
            Pattern.compile("f[u\\*]ck",        Pattern.CASE_INSENSITIVE),
            Pattern.compile("b[i1!]tch",        Pattern.CASE_INSENSITIVE),
            Pattern.compile("sh[i1!]t",         Pattern.CASE_INSENSITIVE),
            Pattern.compile("a[s\\$]{2}",       Pattern.CASE_INSENSITIVE),
            Pattern.compile("n[i1][g9]{2}",     Pattern.CASE_INSENSITIVE)
    );

    // ==============================
    // CACHÉ
    // ==============================

    /*
     * volatile garantiza que todos los hilos lean el valor actualizado.
     * El bloque synchronized en refreshCache() evita que dos hilos
     * recarguen la BD simultáneamente.
     */
    private volatile List<String> cachedBannedWords = List.of();
    private volatile LocalDateTime lastCacheUpdate   = LocalDateTime.MIN;

    public ContentModerationService(BannedWordRepository bannedWordRepository) {
        this.bannedWordRepository = bannedWordRepository;
    }

    // ==============================
    // API PÚBLICA
    // ==============================

    /**
     * Analiza el texto y devuelve el resultado de moderación.
     * Falla rápido — detiene el análisis en la primera regla que no se cumple.
     */
    public ModerationResult analyze(String text) {
        if (text == null || text.isBlank()) return ModerationResult.ok();

        if (text.length() > MAX_TEXT_LENGTH)
            return ModerationResult.rejected("Text exceeds maximum allowed length");

        String normalized = normalize(text);

        if (containsBannedWord(normalized))
            return ModerationResult.rejected("Contains inappropriate language");

        if (containsEvasionPattern(normalized))
            return ModerationResult.rejected("Contains disguised inappropriate content");

        if (isAggressive(text))
            return ModerationResult.rejected("Content appears aggressive");

        if (isSpam(text))
            return ModerationResult.rejected("Content appears to be spam");

        return ModerationResult.ok();
    }

    // ==============================
    // REGLAS DE MODERACIÓN
    // ==============================

    private boolean containsBannedWord(String text) {
        return getBannedWords().stream().anyMatch(text::contains);
    }

    private boolean containsEvasionPattern(String text) {
        return EVASION_PATTERNS.stream().anyMatch(p -> p.matcher(text).find());
    }

    /**
     * Detecta agresividad por exceso de mayúsculas.
     * Solo aplica si el texto tiene suficientes letras para ser significativo.
     */
    private boolean isAggressive(String text) {
        long letters   = text.chars().filter(Character::isLetter).count();
        if (letters < MIN_LETTERS_FOR_UPPERCASE) return false;
        long uppercase = text.chars().filter(Character::isUpperCase).count();
        return (double) uppercase / letters > UPPERCASE_THRESHOLD;
    }

    /** Detecta spam por repetición excesiva de signos de exclamación o interrogación. */
    private boolean isSpam(String text) {
        return SPECIAL_CHAR_PATTERN.matcher(text).find();
    }

    // ==============================
    // CACHÉ
    // ==============================

    private List<String> getBannedWords() {
        if (isCacheExpired()) refreshCache();
        return cachedBannedWords;
    }

    private boolean isCacheExpired() {
        return LocalDateTime.now().isAfter(lastCacheUpdate.plus(CACHE_TTL));
    }

    /**
     * Sincronizado para evitar que múltiples hilos consulten la BD a la vez.
     * El double-check (isCacheExpired dentro del bloque) evita recargas redundantes
     * si varios hilos llegan simultáneamente al bloque.
     */
    private synchronized void refreshCache() {
        if (!isCacheExpired()) return;
        cachedBannedWords = bannedWordRepository.findAll()
                .stream()
                .map(w -> w.getWord().toLowerCase())
                .toList();
        lastCacheUpdate = LocalDateTime.now();
    }

    // ==============================
    // UTILIDADES
    // ==============================

    /**
     * Normaliza el texto para análisis uniforme.
     * Sustituye homoglifos comunes antes de pasar a minúsculas,
     * lo que mejora la detección sin afectar a los patrones regex.
     */
    private String normalize(String text) {
        return text
                .replace('@', 'a')
                .replace('3', 'e')
                .replace('1', 'i')
                .replace('0', 'o')
                .replace('$', 's')
                .replace('€', 'e')
                .toLowerCase()
                .trim();
    }
}
