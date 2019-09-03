package com.igor.langugecards.network.service;

import com.igor.langugecards.network.model.Translate;
import com.igor.langugecards.network.model.TranslateLanguages;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranslatorApi {

    @POST("tr.json/translate")
    Observable<Translate> getTranslate(@Query("key") String key,
                                       @Query("text") String text,
                                       @Query("lang") String lang);

    @POST("tr.json/getLangs")
    Observable<TranslateLanguages> getLangs(@Query("key") String key,
                                            @Query("ui") String languageCode);


}
