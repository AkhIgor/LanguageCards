package com.igor.langugecards.presentation.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory<VM extends ViewModel> implements ViewModelProvider.Factory {

    private Supplier<VM> mViewModelCreator;

    public ViewModelFactory(@NonNull Supplier<VM> viewModelCreator) {
        mViewModelCreator = viewModelCreator;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        VM viewModel = mViewModelCreator.get();

        //noinspection unchecked
        return (T) viewModel;
    }
}
