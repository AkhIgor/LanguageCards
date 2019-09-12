package com.igor.langugecards.presentation.view.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.igor.langugecards.R;
import com.igor.langugecards.presentation.viewmodel.SetTranslateLanguagesViewModel;

import java.util.Collection;
import java.util.List;

public class SetTranslateLanguagesFragment extends ApplicationFragment {

    private TextView mLanguageFrom;
    private TextView mLanguageInto;
    private ListView mLanguagesListFrom;
    private ListView mLanguagesListInto;
    private ProgressBar mProgress;

    private SetTranslateLanguagesViewModel mViewModel;

    private ArrayAdapter<String> mAdapter;

    private SetTranslateLanguagesFragment getInstance() {
        return new SetTranslateLanguagesFragment();
    }

    @Override
    protected void initViews(@NonNull View layout) {
        mProgress = layout.findViewById(R.id.set_translate_language_progress_bar);
        mLanguageFrom = layout.findViewById(R.id.translate_language_from_text_view);
        mLanguageInto = layout.findViewById(R.id.translate_language_to_text_view);
        mLanguagesListFrom = layout.findViewById(R.id.languages_list_from);
        mLanguagesListInto = layout.findViewById(R.id.languages_list_to);
    }

    @Override
    protected void setUpViews() {
        mViewModel.fromLanguageChanged().observe(this,
                newLanguage -> mLanguageFrom.setText(newLanguage));

        mViewModel.targetLanguageChanged().observe(this,
                newLanguage -> mLanguageInto.setText(newLanguage));

        mViewModel.progressChanged().observe(this,
                active -> mProgress.setVisibility(active ? View.VISIBLE : View.GONE));

        mViewModel.languagesSetted().observe(this,
                languages -> setLanguagesToList(languages.values()));

        mLanguageFrom.setOnClickListener(v -> onLanguageSetClickListener(mLanguagesListFrom));
        mLanguageInto.setOnClickListener(v -> onLanguageSetClickListener(mLanguagesListInto));

        mLanguagesListFrom.setOnItemClickListener(this::onListItemClickListener);
        mLanguagesListInto.setOnItemClickListener(this::onListItemClickListener);
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
        mAdapter = new ArrayAdapter<>(getActivity(), languages.size());
        mAdapter.addAll(languages);
        mLanguagesListFrom.setAdapter(mAdapter);
        mLanguagesListInto.setAdapter(mAdapter);
    }

    private void onLanguageSetClickListener(@NonNull ListView list) {
        if (list.getVisibility() == View.VISIBLE) {
            list.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.VISIBLE);
        }
    }

    private void onListItemClickListener(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.languages_list_from: {
                mViewModel.setFromLanguage(mAdapter.getItem(position));
                break;
            }
            case R.id.languages_list_to: {
                mViewModel.setTargetLanguage(mAdapter.getItem(position));
                break;
            }
        }
    }
}
