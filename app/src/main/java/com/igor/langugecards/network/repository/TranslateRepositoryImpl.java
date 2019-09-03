package com.igor.langugecards.network.repository;

import androidx.annotation.NonNull;

import com.igor.langugecards.network.model.Translate;
import com.igor.langugecards.network.model.TranslatorRequest;
import com.igor.langugecards.network.service.NetworkService;
import com.igor.langugecards.network.service.TranslatorApi;

import io.reactivex.Observable;

public class TranslateRepositoryImpl implements TranslateRepository {

    private TranslatorApi mTranslatorApi = NetworkService.getInstance().getJSONApi();

    @Override
    public Observable<Translate> getTranslate(@NonNull TranslatorRequest request) {
        return mTranslatorApi
                .getTranslate(NetworkService.KEY,
                        request.getText(),
                        request.getTranslationDirection());
    }
}
