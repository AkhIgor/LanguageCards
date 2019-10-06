package com.igor.langugecards.model;

import androidx.annotation.DrawableRes;

import com.igor.langugecards.R;

public class ToolbarConfiguration {

    @DrawableRes
    public static final int HOME_BUTTON_RES = R.drawable.ic_cross_24dp;

    private String mTitle;
    private String mSubtitle;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public static ToolbarConfiguration getDefaultToolbarConfiguration() {
        ToolbarConfiguration configuration = new ToolbarConfiguration();
        configuration.setTitle(null);
        configuration.setSubtitle(null);
        return configuration;
    }
}
