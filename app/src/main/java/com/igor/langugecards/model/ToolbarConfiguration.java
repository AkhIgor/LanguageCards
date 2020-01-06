package com.igor.langugecards.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.igor.langugecards.R;

public class ToolbarConfiguration {

    @DrawableRes
    private int mHomeButtonRes;
    private String mTitle;

    public ToolbarConfiguration(@NonNull HomeButton button, @NonNull String toolbarText) {
        switch (button) {
            case ARROW: {
                mHomeButtonRes = R.drawable.ic_arrow_home_24dp;
                break;
            }
            case CROSS: {
                mHomeButtonRes = R.drawable.ic_cross_24dp;
                break;
            }
        }
        mTitle = toolbarText;
    }

    @DrawableRes
    public int getHomeButtonRes() {
        return mHomeButtonRes;
    }

    public String getTitle() {
        return mTitle;
    }
}
