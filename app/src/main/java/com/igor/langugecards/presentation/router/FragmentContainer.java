package com.igor.langugecards.presentation.router;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface FragmentContainer {

    /**
     * @return контекст активити
     */
    @NonNull
    Context getActivityContext();

    /**
     * Показать фрагмент
     *
     * @param fragment фрагмент
     * @param addToBackStack признак, что фрагмент надо добавить в BackStack
     */
    void showFragment(@NonNull Fragment fragment, boolean addToBackStack);

    void showHomeFragment();

    void showLanguagesMenu();

    /**
     * Стартануть активити
     *
     * @param intent интент стартуемого активити
     */
    void startActivity(@NonNull Intent intent);

    /**
     * Удаляет последний фрагмент из backStack
     */
    void popBackStack();

    /**
     * Завершает активити
     */
    void finish();
}
