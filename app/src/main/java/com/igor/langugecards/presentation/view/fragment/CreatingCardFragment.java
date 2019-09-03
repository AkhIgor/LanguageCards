package com.igor.langugecards.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProviders;

import com.igor.langugecards.R;
import com.igor.langugecards.databinding.CreatingCardDataBinding;
import com.igor.langugecards.model.ToolbarConfiguration;
import com.igor.langugecards.network.interactor.TranslateInteractor;
import com.igor.langugecards.presentation.view.activity.MainActivity;
import com.igor.langugecards.presentation.viewmodel.CreatingCardViewModel;
import com.igor.langugecards.presentation.viewmodel.factory.ViewModelFactory;

public class CreatingCardFragment extends ApplicationFragment {

    private CreatingCardViewModel mViewModel;


    public static CreatingCardFragment newInstance() {
        return new CreatingCardFragment();
    }

    private CreatingCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this, new ViewModelFactory<>(
                () -> new CreatingCardViewModel(new TranslateInteractor())))
                .get(CreatingCardViewModel.class);

        CreatingCardDataBinding binding = DataBindingUtil.inflate(inflater,
                getLayoutRes(),
                container,
                false);

        binding.setVariable(BR.viewModel, mViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @LayoutRes
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_creating_card;
    }

    @Override
    protected void initViews(@NonNull View layout) {

    }

    @Override
    protected void setUpViews() {
    }

    @Override
    protected void setToolbar() {
        ToolbarConfiguration configuration = new ToolbarConfiguration();
        String translateFrom = getContext().getString(R.string.translate_from);
        String languageFrom = "";
        configuration.setTitle(translateFrom + " " + languageFrom);
        configuration.setSubtitleRes(R.string.translate_into);
        ((MainActivity) getActivity()).setToolbar(configuration, true);
    }

    @Override
    protected void readArguments() {

    }
}
