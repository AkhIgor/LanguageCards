package com.igor.langugecards.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainMenuViewModel extends ViewModel {

    private SingleLiveEvent<Void> mCreateCardEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> mShowCardsEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> mTestEvent = new SingleLiveEvent<>();

    public LiveData<Void> onCreateCardEvent() {
        return mCreateCardEvent;
    }

    public LiveData<Void> onShowCardsEvent() {
        return mShowCardsEvent;
    }

    public LiveData<Void> onTestEvent() {
        return mTestEvent;
    }

    public void onCreateCardButtonClicked() {
        mCreateCardEvent.call();
    }

    public void onShowCardsButtonClicked() {
        mShowCardsEvent.call();
    }

    public void onTestButtonClicked() {
        mTestEvent.call();
    }
}
