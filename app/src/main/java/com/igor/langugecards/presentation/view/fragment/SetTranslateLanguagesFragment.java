package com.igor.langugecards.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.igor.langugecards.R;
import com.igor.langugecards.databinding.TranslateLanguagesDataBinding;
import com.igor.langugecards.network.interactor.GetLanguagesInteractor;
import com.igor.langugecards.presentation.view.activity.LanguageSettingsListener;
import com.igor.langugecards.presentation.viewmodel.SetTranslateLanguagesViewModel;
import com.igor.langugecards.presentation.viewmodel.factory.ViewModelFactory;

import java.util.Collection;

public class SetTranslateLanguagesFragment extends ApplicationFragment {

    private TextView mLanguageFrom;
    private TextView mLanguageInto;
    private ListView mLanguagesListFrom;
    private ListView mLanguagesListInto;
    private ProgressBar mProgress;

    private SetTranslateLanguagesViewModel mViewModel;

    private ArrayAdapter<String> mAdapter;

    public static SetTranslateLanguagesFragment newInstance() {
        return new SetTranslateLanguagesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this, new ViewModelFactory<>(
                () -> new SetTranslateLanguagesViewModel(requireActivity(),
                        new GetLanguagesInteractor())))
                .get(SetTranslateLanguagesViewModel.class);

        TranslateLanguagesDataBinding binding = DataBindingUtil.inflate(inflater,
                getLayoutRes(),
                container,
                false);

        binding.setVariable(com.igor.langugecards.BR.viewModel, mViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    protected void initViews(@NonNull View layout) {

        mViewModel = ViewModelProviders.of(this, new ViewModelFactory<>(
                () -> new SetTranslateLanguagesViewModel(requireActivity(),
                        new GetLanguagesInteractor())))
                .get(SetTranslateLanguagesViewModel.class);

        mProgress = layout.findViewById(R.id.translate_language_progress_bar);
        mLanguageFrom = layout.findViewById(R.id.translate_language_from_text_view);
        mLanguageInto = layout.findViewById(R.id.translate_language_to_text_view);
        mLanguagesListFrom = layout.findViewById(R.id.languages_list_from);
        mLanguagesListInto = layout.findViewById(R.id.languages_list_to);
    }

    @Override
    protected void setUpViews() {
        mViewModel.fromLanguageChanged().observe(this,
                newLanguage -> {
                    mLanguageFrom.setText(newLanguage);
                    mLanguagesListFrom.setVisibility(View.GONE);
                });

        mViewModel.targetLanguageChanged().observe(this,
                newLanguage -> {
                    mLanguageInto.setText(newLanguage);
                    mLanguagesListInto.setVisibility(View.GONE);
                });

        mViewModel.progressChanged().observe(this,
                active -> mProgress.setVisibility(active ? View.VISIBLE : View.GONE));

        mViewModel.languagesSetted().observe(this,
                languages -> setLanguagesToList(languages.values()));

        mViewModel.onCloseEvent().observe(this, v -> requireActivity().onBackPressed());

        mLanguageFrom.setOnClickListener(v -> onLanguageSetClickListener(mLanguagesListFrom, mLanguagesListInto));
        mLanguageInto.setOnClickListener(v -> onLanguageSetClickListener(mLanguagesListInto, mLanguagesListFrom));

        mLanguagesListFrom.setOnItemClickListener((adapterView, view, position, id) -> onListItemClickListener(adapterView, position));
        mLanguagesListInto.setOnItemClickListener((adapterView, view, position, id) -> onListItemClickListener(adapterView, position));
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void readArguments() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_set_translate_languages;
    }

    private void setLanguagesToList(Collection<String> languages) {
        mAdapter = new ArrayAdapter<>(getActivity(), R.layout.languages_list_item, R.id.language_text_view);
        mAdapter.addAll(languages);
        mLanguagesListFrom.setAdapter(mAdapter);
        mLanguagesListInto.setAdapter(mAdapter);
    }

    private void onLanguageSetClickListener(@NonNull ListView mainList, @NonNull ListView dependedList) {
        if (mainList.getAdapter() != null) {
            if (mainList.getVisibility() == View.VISIBLE) {
                mainList.setVisibility(View.GONE);
            } else {
                mainList.setVisibility(View.VISIBLE);
                if (dependedList.getVisibility() == View.VISIBLE) {
                    dependedList.setVisibility(View.GONE);
                }
            }
        }
    }

    private void onListItemClickListener(View view, int position) {
        switch (view.getId()) {
            case R.id.languages_list_from: {
                mViewModel.setFromLanguage(mAdapter.getItem(position));
                ((LanguageSettingsListener) requireActivity()).onLanguagesChanged();
                break;
            }
            case R.id.languages_list_to: {
                mViewModel.setTargetLanguage(mAdapter.getItem(position));
                ((LanguageSettingsListener) requireActivity()).onLanguagesChanged();
                break;
            }
        }
    }
}
