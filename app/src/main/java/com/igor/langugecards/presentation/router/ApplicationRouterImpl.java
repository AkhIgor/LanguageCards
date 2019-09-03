package com.igor.langugecards.presentation.router;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.igor.langugecards.presentation.view.fragment.CreatingCardFragment;
import com.igor.langugecards.presentation.view.fragment.MainMenuFragment;

import java.lang.ref.WeakReference;

public class ApplicationRouterImpl implements ApplicationRouter {

    private final WeakReference<FragmentContainer> mFragmentContainerRef;

    public ApplicationRouterImpl(@NonNull FragmentContainer fragmentContainer) {
        mFragmentContainerRef = new WeakReference<>(fragmentContainer);
    }

    @Override
    public void goHome() {
        final FragmentContainer container = getContainer();
        if (container != null) {
            container.showFragment(MainMenuFragment.getInstance(), false);
        }
    }

    @Override
    public void createNewCard() {
        final FragmentContainer container = getContainer();
        if (container != null) {
            container.showFragment(CreatingCardFragment.newInstance(), false);
        }
    }

    @Override
    public void showAllCards() {

    }

    @Override
    public void testing() {

    }

    @Nullable
    private FragmentContainer getContainer() {
        return mFragmentContainerRef.get();
    }
}
