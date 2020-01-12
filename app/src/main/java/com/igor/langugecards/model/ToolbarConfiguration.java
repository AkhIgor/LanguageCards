package com.igor.langugecards.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.igor.langugecards.R;
import com.igor.langugecards.presentation.router.ApplicationRouter;

import java.util.function.Consumer;


public class ToolbarConfiguration {

    @DrawableRes
    private int mHomeButtonRes;
    private String mTitle;
    private Consumer<ApplicationRouter> mActionOnButton;

    /**
     * Коснтруктор класса
     * @param button тип кнопки для homeButton
     * @param toolbarText текст на тулбаре
     * @param actionOnButton действие перехода при нажатии на homeButton
     */
    public ToolbarConfiguration(@NonNull HomeButton button, @NonNull String toolbarText, @NonNull Consumer<ApplicationRouter> actionOnButton) {
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
        mActionOnButton = actionOnButton;
    }

    @DrawableRes
    public int getHomeButtonRes() {
        return mHomeButtonRes;
    }

    public String getTitle() {
        return mTitle;
    }

    public Consumer<ApplicationRouter> getActionOnButton() {
        return mActionOnButton;
    }
}
