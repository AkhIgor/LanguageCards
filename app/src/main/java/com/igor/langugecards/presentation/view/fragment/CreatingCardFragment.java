package com.igor.langugecards.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.igor.langugecards.R;
import com.igor.langugecards.database.room.AppDatabase;
import com.igor.langugecards.database.room.DAO.CardInteractor;
import com.igor.langugecards.databinding.CreatingCardDataBinding;
import com.igor.langugecards.model.ToolbarConfiguration;
import com.igor.langugecards.model.TranslateSettings;
import com.igor.langugecards.network.interactor.TranslateInteractor;
import com.igor.langugecards.presentation.view.activity.MainActivity;
import com.igor.langugecards.presentation.viewmodel.CreatingCardViewModel;
import com.igor.langugecards.presentation.viewmodel.factory.ViewModelFactory;

public class CreatingCardFragment extends ApplicationFragment {

    private CreatingCardViewModel mViewModel;
    private TranslateSettings mTanslateSettings;
    private EditText mNativeWordEditText;
    private CardInteractor mCardInteractor;

    public static CreatingCardFragment newInstance() {
        return new CreatingCardFragment();
    }

    private CreatingCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CreatingCardDataBinding binding = DataBindingUtil.inflate(inflater,
                getLayoutRes(),
                container,
                false);

        readArguments();

        mViewModel = ViewModelProviders.of(this, new ViewModelFactory<>(
                () -> new CreatingCardViewModel(getActivity(),
                        new TranslateInteractor(),
                        mCardInteractor)))
                .get(CreatingCardViewModel.class);

        binding.setVariable(com.igor.langugecards.BR.viewModel, mViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.updateTranslateSettings();
    }

    @LayoutRes
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_creating_card;
    }

    @Override
    protected void initViews(@NonNull View layout) {
        mNativeWordEditText = layout.findViewById(R.id.creating_card_native_word);
    }

    @Override
    protected void setUpViews() {
        mViewModel.getTranslateSettings().observe(this, this::showSettings);
//        mNativeWordEditText.addTextChangedListener(mViewModel); - работает точно так же, как и текущая реализация
    }

    @Override
    protected void setToolbar() {
        ToolbarConfiguration configuration = new ToolbarConfiguration();
        String translateFrom = getActivity().getString(R.string.translate_from);
        String languageFrom = "";
        configuration.setTitle(translateFrom + " " + languageFrom);
        String translateInto = getActivity().getString(R.string.translate_into);
        String languageInto = "";
        configuration.setSubtitle(translateInto + " " + languageInto);
        ((MainActivity) getActivity()).setToolbar(configuration, true);
    }

    @Override
    protected void readArguments() {
        mCardInteractor = AppDatabase
                .getInstance(requireActivity())
                .getCardInteractor();
    }

    private void showSettings(TranslateSettings settings) {
        ToolbarConfiguration configuration = new ToolbarConfiguration();
        String translateFrom = getActivity().getString(R.string.translate_from);
        String languageFrom = settings.getLanguageFrom();
        configuration.setTitle(translateFrom + " " + languageFrom);
        String translateInto = getActivity().getString(R.string.translate_into);
        String languageInto = settings.getLanguageTo();
        configuration.setSubtitle(translateInto + " " + languageInto);
        ((MainActivity) getActivity()).setToolbar(configuration, true);
    }
}
