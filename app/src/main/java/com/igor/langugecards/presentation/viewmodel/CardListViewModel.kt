package com.igor.langugecards.presentation.viewmodel

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor.langugecards.database.room.DAO.CardInteractor
import com.igor.langugecards.model.Card
import io.reactivex.disposables.CompositeDisposable

class CardListViewModel(
        private val mCardInteractor: CardInteractor) : ViewModel() {

    val mProgressEvent = MutableLiveData<Boolean>()
    val mCards = MutableLiveData<List<Card>>()
    private val mDisposable = CompositeDisposable()

    fun onScreenStart() {
        loadCards()
    }

    private fun loadCards() {
        mDisposable.add(mCardInteractor.getAllCards()
                .doOnSubscribe { mProgressEvent.postValue(true) }
//                .doAfterTerminate { mProgressEvent.postValue(false) }
                .subscribe(
                        { cards ->
                            mCards.postValue(cards)
                            mProgressEvent.postValue(false)
                        },
                        { error ->
                            handleError(error)
                        }
                )
        )
    }

    private fun handleError(@NonNull throwable: Throwable) {
        mProgressEvent.postValue(false)
    }
}