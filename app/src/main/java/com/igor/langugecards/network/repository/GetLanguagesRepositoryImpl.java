package com.igor.langugecards.network.repository;

import com.igor.langugecards.network.model.TranslateLanguages;
import com.igor.langugecards.network.service.NetworkService;
import com.igor.langugecards.network.service.TranslatorApi;

import io.reactivex.Observable;

public class GetLanguagesRepositoryImpl implements GetLanguagesRepository {

    private TranslatorApi mTranslatorApi = NetworkService.getInstance().getJSONApi();
    private TranslateLanguages mLanguages;

    @Override
    public Observable<TranslateLanguages> getLanguages() {
        if (mLanguages == null) {
            return mTranslatorApi
                    .getLangs(NetworkService.KEY, "ru")
                    .map(langs -> mLanguages = langs);
        } else {
            return Observable.just(mLanguages);
        }
    }
}
