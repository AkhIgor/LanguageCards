package com.igor.langugecards.network.service;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    public static final String KEY = "trnsl.1.1.20190810T093306Z.04588084d0e78908.cbe85c78e9c766026901850ea42aa12429744ebd";

    private static NetworkService mInstance;
    private static final String BASE_URL = "https://translate.yandex.net/api/v1.5/";
    private Retrofit mRetrofit;

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public TranslatorApi getJSONApi() {
        return mRetrofit.create(TranslatorApi.class);
    }
}
