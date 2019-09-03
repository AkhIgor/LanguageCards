package com.igor.langugecards.presentation.viewmodel;

import androidx.databinding.InverseMethod;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Converter<T> {

    public static LiveData<String> convertStringToLiveData(String value) {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        liveData.postValue(value);
        return liveData;
    }

    @InverseMethod("convertStringToLiveData")
    public static String convertLiveDataToString(LiveData<String> liveData) {
        return liveData.getValue();
    }
}
