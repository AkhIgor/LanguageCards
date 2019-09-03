package com.igor.langugecards.network.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Translate {

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("lang")
    @Expose
    private String lang;

    @SerializedName("text")
    @Expose
    private String[] text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return TextUtils.join("", text);
    }

    public void setText(String[] text) {
        this.text = text;
    }
}
