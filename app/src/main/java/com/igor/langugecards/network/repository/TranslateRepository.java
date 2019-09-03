package com.igor.langugecards.network.repository;

import androidx.annotation.NonNull;

import com.igor.langugecards.network.model.TranslatorRequest;
import com.igor.langugecards.network.model.Translate;

import io.reactivex.Observable;

public interface TranslateRepository {

    Observable<Translate> getTranslate(@NonNull TranslatorRequest request);
}
