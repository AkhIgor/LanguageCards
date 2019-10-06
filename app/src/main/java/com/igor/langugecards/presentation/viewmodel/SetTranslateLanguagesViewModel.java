package com.igor.langugecards.presentation.viewmodel;

import android.content.Context;
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

import static com.igor.langugecards.database.TranslateSettingInteractor.FROM;
import static com.igor.langugecards.database.TranslateSettingInteractor.TO;

public class SetTranslateLanguagesViewModel extends ViewModel {

    private Context mContext;
    private GetLanguagesInteractor mInteractor;
    private CompositeDisposable mDisposable;

    private MutableLiveData<String> mFromLanguageChangedAction = new MutableLiveData<>();
    private MutableLiveData<String> mTargetLanguageChangedAction = new MutableLiveData<>();
    private MutableLiveData<Boolean> mProgress = new MutableLiveData<>();
    private MutableLiveData<Map<String, String>> mPossibleLanguages = new MutableLiveData<>();

    public SetTranslateLanguagesViewModel(@NonNull Context context,
                                          @NonNull GetLanguagesInteractor interactor) {
        mContext = context;
        mInteractor = interactor;
        mDisposable = new CompositeDisposable();

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

        TranslateSettingInteractor.writeTranslateSettings(FROM,
                mContext,
                language,
                findLanguageCode(language));
    }

    public void setTargetLanguage(String language) {
        mTargetLanguageChangedAction.postValue(language);
        TranslateSettingInteractor.writeTranslateSettings(TO,
                mContext,
                language,
                findLanguageCode(language));
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
