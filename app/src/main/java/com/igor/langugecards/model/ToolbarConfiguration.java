package com.igor.langugecards.model;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.igor.langugecards.R;

public class ToolbarConfiguration {

    public static final int NO_HOME_BUTTON = -1;

    @DrawableRes
    private int mHomeButton;

    private String mTitle;

    private String mSubtitle;

    public int getHomeButton() {
        return mHomeButton;
    }

    public void setHomeButton(int homeButton) {
        mHomeButton = homeButton;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String subtitleRes) {
        mSubtitle = subtitleRes;
    }


    public static ToolbarConfiguration defaultToolbarConfiguration(@NonNull Context context) {
        ToolbarConfiguration configuration = new ToolbarConfiguration();
        configuration.setHomeButton(NO_HOME_BUTTON);
        configuration.setTitle(context.getString(R.string.app_name));
        configuration.setSubtitle(null);
        return configuration;
    }
}
