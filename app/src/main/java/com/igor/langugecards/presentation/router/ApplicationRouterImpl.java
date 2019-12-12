package com.igor.langugecards.presentation.router;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.igor.langugecards.presentation.view.fragment.CardListFragment;
import com.igor.langugecards.presentation.view.fragment.CreatingCardFragment;
import com.igor.langugecards.presentation.view.fragment.LearningCardsFragment;

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
            container.showHomeFragment();
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
        final FragmentContainer container = getContainer();
        if (container != null) {
            container.showFragment(CardListFragment.CardListFragmentFactory.newInstance(), false);
        }
    }

    @Override
    public void testing() {
        final FragmentContainer container = getContainer();
        if (container != null) {
            container.showFragment(LearningCardsFragment.LearningCardsFragmentFactory.newInstance(), true);
        }
    }

    @Nullable
    private FragmentContainer getContainer() {
        return mFragmentContainerRef.get();
    }
}
