package com.igor.langugecards.presentation.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.igor.langugecards.database.TranslateSettingInteractor;
import com.igor.langugecards.network.interactor.GetLanguagesInteractor;

import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;

public class SetTranslateLanguagesViewModel extends ViewModel {

    private static final String FROM_LANGUAGE = "from language";
    private static final String TO_LANGUAGE = "to language";

    private GetLanguagesInteractor mInteractor;
    private TranslateSettingInteractor mSettingsInteractor;
    private CompositeDisposable mDisposable;

    private MutableLiveData<String> mFromLanguageChangedAction = new MutableLiveData<>();
    private MutableLiveData<String> mTargetLanguageChangedAction = new MutableLiveData<>();
    private MutableLiveData<Boolean> mProgress = new MutableLiveData<>();
    private MutableLiveData<Map<String, String>> mPossibleLanguages = new MutableLiveData<>();

    public SetTranslateLanguagesViewModel(@NonNull GetLanguagesInteractor interactor,
                                          @NonNull TranslateSettingInteractor settingsInteractor,
                                          @NonNull CompositeDisposable disposable) {
        mInteractor = interactor;
        mSettingsInteractor = settingsInteractor;
        mDisposable = disposable;

        getLanguages();
    }

    public LiveData<String> fromLanguageChanged() {
        return mFromLanguageChangedAction;
    }

    public LiveData<String> targetLanguageChanged() {
        return mTargetLanguageChangedAction;
    }

    public LiveData<Boolean> progressChanged() {
        return mProgress;
    }

    public LiveData<Map<String, String>> languagesSetted() {
        return mPossibleLanguages;
    }

    public void setFromLanguage(String language) {
        mFromLanguageChangedAction.postValue(language);

        mSettingsInteractor.writeTranslateSettings(FROM_LANGUAGE,
                findLanguageCode(language),
                language);
    }

    public void setTargetLanguage(String language) {
        mFromLanguageChangedAction.postValue(language);
        mSettingsInteractor.writeTranslateSettings(TO_LANGUAGE,
                findLanguageCode(language),
                language);
    }

    private void getLanguages() {
        mDisposable.add(mInteractor.getLanguages()
                .doOnSubscribe(unsused -> mProgress.postValue(true))
                .doAfterTerminate(() -> mProgress.postValue(false))
                .subscribe(result -> mPossibleLanguages.postValue(result.getLanguageCodes()),
                        this::handleError));
    }

    private void handleError(@NonNull Throwable error) {
        Log.d("SetTranslateLanguages", error.getMessage());
    }

    @Nullable
    private String findLanguageCode(@NonNull String language) {
        Map<String, String> languages = mPossibleLanguages.getValue();
        if (languages != null) {
            String foundKey;
            Set<String> keys = languages.keySet();
            for (String key : keys) {
                if (language.equals(languages.get(key))) {
                    foundKey = key;
                    return foundKey;
                }
            }
        }
        return null;
    }
}
