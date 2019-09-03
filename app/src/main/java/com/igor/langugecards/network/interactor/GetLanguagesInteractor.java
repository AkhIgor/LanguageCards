package com.igor.langugecards.network.interactor;

import com.igor.langugecards.network.model.TranslateLanguages;
import com.igor.langugecards.network.repository.GetLanguagesRepository;
import com.igor.langugecards.network.repository.GetLanguagesRepositoryImpl;

import io.reactivex.Observable;

public class GetLanguagesInteractor {

    private GetLanguagesRepository mRepository = new GetLanguagesRepositoryImpl();

    public Observable<TranslateLanguages> getLanguages() {
        return mRepository.getLanguages();
    }
}
