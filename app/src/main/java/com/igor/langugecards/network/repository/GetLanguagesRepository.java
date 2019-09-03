package com.igor.langugecards.network.repository;

import com.igor.langugecards.network.model.TranslateLanguages;

import io.reactivex.Observable;

public interface GetLanguagesRepository {

    Observable<TranslateLanguages> getLanguages();
}
