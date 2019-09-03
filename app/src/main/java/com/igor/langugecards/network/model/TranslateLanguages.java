package com.igor.langugecards.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class TranslateLanguages {

    @SerializedName("dirs")
    @Expose
    private List<String> mTranslateDirs;

    @SerializedName("langs")
    @Expose
    private Map<String, String> mLanguageCodes;

    public List<String> getTranslateDirs() {
        return mTranslateDirs;
    }

    public void setTranslateDirs(List<String> translateDirs) {
        mTranslateDirs = translateDirs;
    }

    public Map<String, String> getLanguageCodes() {
        return mLanguageCodes;
    }

    public void setLanguageCodes(Map<String, String> languageCodes) {
        mLanguageCodes = languageCodes;
    }
}
