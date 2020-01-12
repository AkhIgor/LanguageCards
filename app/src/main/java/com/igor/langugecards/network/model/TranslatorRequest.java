package com.igor.langugecards.network.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TranslatorRequest {

    private final String mText;
    private final String mTranslationDirection;

    private TranslatorRequest(@NonNull String text, @NonNull String translateDirect) {
        mText = text;
        mTranslationDirection = translateDirect;
    }

    public static TranslatorRequest createRequest(@NonNull String text, @NonNull String nativeLanguage, @NonNull String targetLanguage) {
        if (!nativeLanguage.isEmpty()) {
            String translateDirection = getTranslateDirection(nativeLanguage, targetLanguage);
            return new TranslatorRequest(text, translateDirection);
        } else {
            return new TranslatorRequest(text, targetLanguage);
        }
    }

    public String getText() {
        return mText;
    }

    public String getTranslationDirection() {
        return mTranslationDirection;
    }

    private static String getTranslateDirection(@NonNull String nativeLanguage, @NonNull String targetLanguage) {
        return nativeLanguage + '-' + targetLanguage;
    }
}
