package com.igor.langugecards.presentation.view.fragment;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.igor.langugecards.R;
import com.igor.langugecards.model.ToolbarConfiguration;
import com.igor.langugecards.presentation.view.activity.MainActivity;
import com.igor.langugecards.presentation.viewmodel.MainMenuViewModel;

import static com.igor.langugecards.model.ToolbarConfiguration.defaultToolbarConfiguration;

public class MainMenuFragment extends ApplicationFragment {

    private Button mCreateCardButton;
    private Button mShowCardsButton;
    private Button mTestButton;

    private MainMenuViewModel mViewModel;

    public static MainMenuFragment getInstance() {
        return new MainMenuFragment();
    }

    @Override
    protected void initViews(@NonNull View layout) {
        mCreateCardButton = layout.findViewById(R.id.create_card_button);
        mShowCardsButton = layout.findViewById(R.id.show_cards_button);
        mTestButton = layout.findViewById(R.id.test_button);

        mViewModel = ViewModelProviders.of(this)
                .get(MainMenuViewModel.class);
    }

    @Override
    protected void setUpViews() {
        mCreateCardButton.setOnClickListener(v -> mViewModel.onCreateCardButtonClicked());
        mShowCardsButton.setOnClickListener(v -> mViewModel.onShowCardsButtonClicked());
        mTestButton.setOnClickListener(v -> mViewModel.onTestButtonClicked());

        mViewModel.onCreateCardEvent().observe(this, observer -> getRouter().createNewCard());
        mViewModel.onShowCardsEvent().observe(this, observer -> getRouter().showAllCards());
        mViewModel.onTestEvent().observe(this, observer -> getRouter().testing());
    }

    @Override
    protected void setToolbar() {
        ((MainActivity) getActivity()).setToolbar(defaultToolbarConfiguration(getActivity()));
    }

    @Override
    protected void readArguments() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_main_menu;
    }
}
