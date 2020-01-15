package com.igor.langugecards.presentation.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.igor.langugecards.constants.Constants;
import com.igor.langugecards.database.preferences.TranslateSettingInteractor;
import com.igor.langugecards.network.interactor.GetLanguagesInteractor;
import com.igor.langugecards.network.model.TranslateLanguages;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;

import static com.igor.langugecards.database.preferences.TranslateSettingInteractor.FROM;
import static com.igor.langugecards.database.preferences.TranslateSettingInteractor.TO;

public class SetTranslateLanguagesViewModel extends ViewModel {

    private GetLanguagesInteractor mInteractor;
    private TranslateSettingInteractor mTranslateInteractor;
    private CompositeDisposable mDisposable;

    private MutableLiveData<String> mFromLanguageChangedAction = new MutableLiveData<>();
    private MutableLiveData<String> mTargetLanguageChangedAction = new MutableLiveData<>();
    private MutableLiveData<Boolean> mProgress = new MutableLiveData<>();
    private MutableLiveData<Map<String, String>> mPossibleLanguages = new MutableLiveData<>();

    private SingleLiveEvent<Void> mCloseEvent = new SingleLiveEvent<>();

    public SetTranslateLanguagesViewModel(@NonNull GetLanguagesInteractor interactor,
                                          @NonNull TranslateSettingInteractor translateInteractor) {
        mInteractor = interactor;
        mTranslateInteractor = translateInteractor;
        mDisposable = new CompositeDisposable();

        getLanguages();
        showSettedLanguages();
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

    public LiveData<Void> onCloseEvent() {
        return mCloseEvent;
    }

    public void closeEvent() {
        mCloseEvent.call();
    }

    public void setFromLanguage(String language) {
        mFromLanguageChangedAction.postValue(language);

        mTranslateInteractor.writeTranslateSettings(FROM,
                language,
                findLanguageCode(language));
    }

    public void setTargetLanguage(String language) {
        mTargetLanguageChangedAction.postValue(language);
        mTranslateInteractor.writeTranslateSettings(TO,
                language,
                findLanguageCode(language));
    }

    private void showSettedLanguages() {
        mFromLanguageChangedAction.setValue(mTranslateInteractor.readTranslateSettings()
                .getLanguageFrom());
        mTargetLanguageChangedAction.setValue(mTranslateInteractor.readTranslateSettings()
                .getLanguageTo());
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
            return Constants.EMPTY_STRING;
        }
        return null;
    }

    //Работает некорректно - можно убрать потом
    //Оставил на всякий случай
    private TranslateLanguages sortLanguagesByAlphabet(@NonNull TranslateLanguages languages) {
        Map<String, String> sortedMap = new HashMap<>();
        String[] orderedLanguages = languages.getLanguageCodes().values().toArray(new String[0]);
        String[] orderedKeys = languages.getLanguageCodes().keySet().toArray(new String[0]);
        String buffer;

        for (int currentPosition = 0; currentPosition < orderedLanguages.length - 1; currentPosition++) {
            for (int nextPosition = 1; nextPosition < orderedLanguages.length; nextPosition++) {
                if (orderedLanguages[currentPosition].compareTo(orderedLanguages[nextPosition]) > 0) {
                    buffer = orderedLanguages[currentPosition];
                    orderedLanguages[currentPosition] = orderedLanguages[nextPosition];
                    orderedLanguages[nextPosition] = buffer;

                    buffer = orderedKeys[currentPosition];
                    orderedKeys[currentPosition] = orderedKeys[nextPosition];
                    orderedKeys[nextPosition] = buffer;
                }
            }
        }
        for (int languagePos = 0; languagePos < languages.getLanguageCodes().size(); languagePos++) {
            sortedMap.put(orderedKeys[languagePos], orderedLanguages[languagePos]);
        }

        languages.setLanguageCodes(sortedMap);

        return languages;
    }
}
