package com.igor.langugecards.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.igor.langugecards.R;
import com.igor.langugecards.database.preferences.TranslateSettingInteractor;
import com.igor.langugecards.database.room.AppDatabase;
import com.igor.langugecards.database.room.DAO.CardInteractor;
import com.igor.langugecards.databinding.CreatingCardDataBinding;
import com.igor.langugecards.model.HomeButton;
import com.igor.langugecards.model.ToolbarConfiguration;
import com.igor.langugecards.model.TranslateSettings;
import com.igor.langugecards.network.interactor.TranslateInteractor;
import com.igor.langugecards.presentation.router.ApplicationRouter;
import com.igor.langugecards.presentation.view.activity.LanguageSettingsListener;
import com.igor.langugecards.presentation.view.activity.MainActivity;
import com.igor.langugecards.presentation.viewmodel.CreatingCardViewModel;
import com.igor.langugecards.presentation.viewmodel.factory.ViewModelFactory;

public class CreatingCardFragment extends ApplicationFragment
        implements LanguageSettingsListener {

    public static final String FRAGMENT_TAG = "CreatingCardFragment";

    private CreatingCardViewModel mViewModel;

    private TextView mFromLanguageTextView;
    private TextView mToLanguageTextView;
    private View mTranslateArrow;

    private CardInteractor mCardInteractor;

    public static CreatingCardFragment newInstance() {
        return new CreatingCardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CreatingCardDataBinding binding = DataBindingUtil.inflate(inflater,
                getLayoutRes(),
                container,
                false);

        readArguments();

        mViewModel = ViewModelProviders.of(this, new ViewModelFactory<>(
                () -> new CreatingCardViewModel(
                        new TranslateInteractor(),
                        mCardInteractor,
                        new TranslateSettingInteractor(requireActivity()))
        ))
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
        mFromLanguageTextView = layout.findViewById(R.id.from_language_text_view);
        mToLanguageTextView = layout.findViewById(R.id.to_language_text_view);
        mTranslateArrow = layout.findViewById(R.id.trnaslte_arrow_icon);
    }

    @Override
    protected void setUpViews() {
        mViewModel.getTranslateSettings().observe(this, this::showSettings);
//        mNativeWordEditText.addTextChangedListener(mViewModel); - работает точно так же, как и текущая реализация
        mViewModel.getOperationStatusEvent().observe(this, stringRes -> showMessage(stringRes));

        mFromLanguageTextView.setOnClickListener(v -> openLanguagesFragment());
        mToLanguageTextView.setOnClickListener(v -> openLanguagesFragment());
        mTranslateArrow.setOnClickListener(v -> openLanguagesFragment());
    }

    @Override
    protected void setToolbar() {
        ToolbarConfiguration configuration = new ToolbarConfiguration(HomeButton.LIST, requireActivity().getString(R.string.translate_fragment_title), ApplicationRouter::showAllCards);
        ((MainActivity) requireActivity()).setToolbar(configuration);

    }

    @Override
    protected void readArguments() {
        mCardInteractor = AppDatabase
                .getInstance(requireActivity())
                .getCardInteractor();
    }

    private void showSettings(TranslateSettings settings) {
        mFromLanguageTextView.setText(settings.getLanguageFrom());
        mToLanguageTextView.setText(settings.getLanguageTo());
    }

    private void showMessage(@StringRes int messageId) {
        Toast.makeText(requireActivity(), requireActivity().getString(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLanguagesChanged() {
        mViewModel.readTranslateSettings();
    }

    private void openLanguagesFragment() {
        getRouter().showSettingsMenu();
    }
}
