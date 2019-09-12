package com.igor.langugecards.model;

import androidx.annotation.DrawableRes;

import com.igor.langugecards.R;

public class ToolbarConfiguration {

    private boolean mLogoRes;

    private String mTitle;

    private String mSubtitle;
    @DrawableRes
    private int mHomeButtonRes;

    public boolean isLogoRes() {
        return mLogoRes;
    }

    public void setLogoRes(boolean logoRes) {
        mLogoRes = logoRes;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubtitleRes() {
        return mSubtitle;
    }

    public void setSubtitleRes(String subtitleRes) {
        mSubtitle = subtitleRes;
    }

    public int getHomeButtonRes() {
        return mHomeButtonRes;
    }

    public void setHomeButtonRes(int homeButtonRes) {
        mHomeButtonRes = homeButtonRes;
    }

    public static ToolbarConfiguration getDefaultToolbarConfiguration() {
        ToolbarConfiguration configuration = new ToolbarConfiguration();
        configuration.setLogoRes(true);
        return configuration;
    }
}
