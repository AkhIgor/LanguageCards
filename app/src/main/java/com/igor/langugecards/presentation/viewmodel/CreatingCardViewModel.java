package com.igor.langugecards.presentation.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.igor.langugecards.model.Card;
import com.igor.langugecards.network.model.Translate;
import com.igor.langugecards.network.interactor.TranslateInteractor;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class CreatingCardViewModel extends ViewModel {

    private final TranslateInteractor mTranslateInteractor;
    private final CompositeDisposable mDisposable;

    private String mTheme;
    private MutableLiveData<String> mNativeWord = new MutableLiveData<>();
    private MutableLiveData<String> mFromLanguage = new MutableLiveData<>();
    private MutableLiveData<String> mToLanguage = new MutableLiveData<>();

    private MutableLiveData<String> mTranslate = new MutableLiveData<>();
    private MutableLiveData<String> mTranscription = new MutableLiveData<>();

    private MutableLiveData<Boolean> mProgress = new MutableLiveData<>();

    private BehaviorSubject<String> mUserInputSubject = BehaviorSubject.create();

    public CreatingCardViewModel(@NonNull TranslateInteractor translateInteractor) {
        mTranslateInteractor = translateInteractor;
        mDisposable = new CompositeDisposable();

        mDisposable.add(mUserInputSubject
                        .debounce(250, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(this::translate)
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

    public void saveCard() {
        if (mFromLanguage.getValue() != null &&
                !mFromLanguage.getValue().isEmpty() &&
                mToLanguage.getValue() != null &&
                mNativeWord.getValue() != null &&
                mTranslate.getValue() != null) {

            Card card = new Card(mFromLanguage.getValue(),
                    mToLanguage.getValue(),
                    mTheme,
                    mNativeWord.getValue(),
                    mTranscription.getValue(),
                    mTranslate.getValue());
        }
    }

    public void onTranslatedWordChanged(@NonNull CharSequence text) {
        if (text.toString().isEmpty()) {
            mTranslate.postValue("");
        } else {
            mUserInputSubject.onNext(text.toString());
        }
    }

    private void translate(@NonNull String translatedWord) {
        mDisposable.add(
                mTranslateInteractor.translate(translatedWord,
                        mFromLanguage.getValue(),
                        "ru")
                        .doOnSubscribe(unused -> mProgress.postValue(true))
                        .doAfterTerminate(() -> mProgress.postValue(false))
                        .subscribe(this::setTranslate,
                                this::handleError));
    }

    private void setTranslate(@NonNull Translate translate) {
        mTranslate.postValue(translate.getText());
    }

    private void handleError(@NonNull Throwable throwable) {
        Log.d("Request translate error", throwable.getMessage());
    }
}
