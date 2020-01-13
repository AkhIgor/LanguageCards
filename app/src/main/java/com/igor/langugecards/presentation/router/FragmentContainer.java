package com.igor.langugecards.presentation.router;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface FragmentContainer {

    /**
     * Показать фрагмент
     *
     * @param fragment       фрагмент
     */
    void showFragment(@NonNull Fragment fragment);

    void showHomeFragment();

    void showLanguagesMenu();
}
