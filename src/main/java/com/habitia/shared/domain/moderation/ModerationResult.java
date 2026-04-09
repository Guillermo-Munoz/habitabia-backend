package com.habitia.shared.domain.moderation;

public record ModerationResult(
    boolean passed,
    String reason
) {
    public static ModerationResult ok(){
        return new ModerationResult(true, null);
    }

    public static ModerationResult rejected(String reason){
        return new ModerationResult(false, reason);
    }
}
