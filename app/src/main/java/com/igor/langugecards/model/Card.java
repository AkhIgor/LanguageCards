package com.igor.langugecards.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Card {

    @PrimaryKey(autoGenerate = true)
    private long mId;
    private String mFromLanguage;
    private String mToLanguage;
    private String mTheme;
    private String mNativeWord;
    private String mTranscription;
    private String mTranslatedWord;

    public Card(@NonNull String fromLanguage,
                @NonNull String toLanguage,
                @Nullable String theme,
                @NonNull String nativeWord,
                @Nullable String transcription,
                @NonNull String translatedWord) {
        mFromLanguage = fromLanguage;
        mToLanguage = toLanguage;
        mTheme = theme;
        mNativeWord = nativeWord;
        mTranscription = transcription;
        mTranslatedWord = translatedWord;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getFromLanguage() {
        return mFromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        mFromLanguage = fromLanguage;
    }

    public String getToLanguage() {
        return mToLanguage;
    }

    public void setToLanguage(String toLanguage) {
        mToLanguage = toLanguage;
    }

    public String getTheme() {
        return mTheme;
    }

    public void setTheme(String theme) {
        mTheme = theme;
    }

    public String getNativeWord() {
        return mNativeWord;
    }

    public void setNativeWord(String nativeWord) {
        mNativeWord = nativeWord;
    }

    public String getTranscription() {
        return mTranscription;
    }

    public void setTranscription(String transcription) {
        mTranscription = transcription;
    }

    public String getTranslatedWord() {
        return mTranslatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        mTranslatedWord = translatedWord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return mId == card.mId &&
                Objects.equals(mToLanguage, card.mToLanguage) &&
                Objects.equals(mFromLanguage, card.mFromLanguage) &&
                Objects.equals(mTheme, card.mTheme) &&
                Objects.equals(mNativeWord, card.mNativeWord) &&
                Objects.equals(mTranscription, card.mTranscription) &&
                Objects.equals(mTranslatedWord, card.mTranslatedWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mToLanguage, mFromLanguage,
                mTheme, mNativeWord, mTranscription, mTranslatedWord);
    }
}
