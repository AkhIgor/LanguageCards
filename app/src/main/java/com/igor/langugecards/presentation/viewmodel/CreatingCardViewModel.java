package com.igor.langugecards.presentation.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.igor.langugecards.database.preferences.TranslateSettingInteractor;
import com.igor.langugecards.database.room.DAO.CardInteractor;
import com.igor.langugecards.model.Card;
import com.igor.langugecards.model.TranslateSettings;
import com.igor.langugecards.network.interactor.TranslateInteractor;
import com.igor.langugecards.network.model.Translate;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class CreatingCardViewModel extends AndroidViewModel {

    private static final String EMPTY_STRING = "";

    private final Application mApplication;
    private final TranslateInteractor mTranslateInteractor;
    private final CompositeDisposable mDisposable;
    private final CardInteractor mCardInteractor;

    private String mTheme;
    private MutableLiveData<String> mNativeWord = new MutableLiveData<>();
    private MutableLiveData<String> mFromLanguage = new MutableLiveData<>();
    private MutableLiveData<String> mToLanguage = new MutableLiveData<>();

    private MutableLiveData<String> mTranslate = new MutableLiveData<>();
    private MutableLiveData<String> mTranscription = new MutableLiveData<>();

    private MutableLiveData<Boolean> mProgress = new MutableLiveData<>();

    private BehaviorSubject<String> mUserInputSubject = BehaviorSubject.create();

    private MutableLiveData<TranslateSettings> mTranslateSettings = new MutableLiveData<>();

    private String mFromLanguageCode;
    private String mToLanguageCode;

    public CreatingCardViewModel(@NonNull Application application,
                                 @NonNull TranslateInteractor translateInteractor,
                                 @NonNull CardInteractor cardInteractor) {
        super(application);
        mApplication = application;
        mTranslateInteractor = translateInteractor;
        mDisposable = new CompositeDisposable();
        mCardInteractor = cardInteractor;

        mDisposable.add(mUserInputSubject
                .debounce(250, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::translate)
        );
    }

    public String getTheme() {
        return mTheme;
    }

    public void setTheme(String theme) {
        mTheme = theme;
    }

    public LiveData<String> getNativeWord() {
        return mNativeWord;
    }

    public LiveData<String> getFromLanguage() {
        return mFromLanguage;
    }

    public LiveData<String> getToLanguage() {
        return mToLanguage;
    }

    public LiveData<String> getTranslate() {
        return mTranslate;
    }

    public LiveData<String> getTranscription() {
        return mTranscription;
    }

    public LiveData<Boolean> getProgress() {
        return mProgress;
    }

    public LiveData<TranslateSettings> getTranslateSettings() {
        return mTranslateSettings;
    }

    public void saveCard() {
        mDisposable.add(
                createCard()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(unused -> mProgress.postValue(true))
                        .subscribe(this::addToDatabase,
                                this::handleError));
    }

    public void onTranslatedWordChanged(@NonNull CharSequence text) {
        if (text.toString().isEmpty()) {
            mTranslate.postValue(EMPTY_STRING);
        } else {
            mUserInputSubject.onNext(text.toString());
        }
    }

    public void updateTranslateSettings() {
        readTranslateSettings();
    }

    private void translate(@NonNull String translatedWord) {
        mDisposable.add(
                mTranslateInteractor.translate(translatedWord,
                        mFromLanguageCode,
                        mToLanguageCode)
                        .doOnSubscribe(unused -> mProgress.postValue(true))
                        .doAfterTerminate(() -> mProgress.postValue(false))
                        .subscribe(translate -> {
                                    setTranslate(translate);
                                    mNativeWord.postValue(translatedWord);
                                },
                                this::handleError));
    }

    private void readTranslateSettings() {
        TranslateSettings translateSettings = TranslateSettingInteractor.readTranslateSettings(mApplication.getApplicationContext());
        if (translateSettings.getLanguageCodeFrom() == null ||
                translateSettings.getLanguageCodeFrom().isEmpty()) {
            translateSettings.setLanguageFrom(translateSettings.getAutoDetectingLanguage());
        }
        mFromLanguageCode = translateSettings.getLanguageCodeFrom();
        mToLanguageCode = translateSettings.getLanguageCodeTo();

        mTranslateSettings.postValue(translateSettings);
    }

    private void setTranslate(@NonNull Translate translate) {
        mTranslate.postValue(translate.getText());
    }

    private void handleError(@NonNull Throwable throwable) {
        Log.d("Request translate error", throwable.getMessage());
    }

    private Observable<Card> createCard() {
        return Observable.just(new Card(
                mFromLanguage.getValue(),
                mToLanguage.getValue(),
                mTheme,
                mNativeWord.getValue(),
                mTranscription.getValue(),
                mTranslate.getValue()));
    }

    private void addToDatabase(@NonNull Card card) {
        mDisposable.add(
                Completable.fromAction(() -> mCardInteractor.addCard(card))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate(() -> mProgress.setValue(false))
                        .subscribe(this::showMessage));
    }

    private void showMessage() {
        Toast.makeText(mApplication, "Success", Toast.LENGTH_SHORT).show();
    }
}
