package com.igor.langugecards.network.interactor;

import androidx.annotation.Nullable;

import com.igor.langugecards.network.model.Translate;
import com.igor.langugecards.network.model.TranslatorRequest;
import com.igor.langugecards.network.repository.TranslateRepository;
import com.igor.langugecards.network.repository.TranslateRepositoryImpl;

import io.reactivex.Observable;

public class TranslateInteractor {

    private TranslateRepository mRepository = new TranslateRepositoryImpl();

    public Observable<Translate> translate(@Nullable String text, @Nullable String from, @Nullable String to) {
        if (text != null && to != null) {
            TranslatorRequest request = TranslatorRequest.createRequest(text, from, to);
            return mRepository.getTranslate(request);
        } else {
            return Observable.error(new Throwable());
        }
    }
}
