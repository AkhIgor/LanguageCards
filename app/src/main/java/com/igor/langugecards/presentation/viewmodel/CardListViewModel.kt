package com.igor.langugecards.presentation.viewmodel

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor.langugecards.database.room.DAO.CardInteractor
import com.igor.langugecards.model.Card
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CardListViewModel(
        private val mCardInteractor: CardInteractor) : ViewModel() {

    val mProgressEvent = MutableLiveData<Boolean>()
    val mCards = MutableLiveData<List<Card>>()
    private val mDisposable = CompositeDisposable()

    fun onScreenStart() {
        loadCards()
    }

    fun removeCard(card: Card) {
        mDisposable.add(
                Completable.fromAction { mCardInteractor.deleteCard(card) }
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                { showMessage() },
                                { handleError(it) }
                        )
        )
    }

    private fun loadCards() {
        mDisposable.add(mCardInteractor.getAllCards()
                .doOnSubscribe { mProgressEvent.postValue(true) }
                .subscribe(
                        { cards ->
                            mCards.postValue(cards)
                            mProgressEvent.postValue(false)
                            mDisposable.clear()
                        },
                        { error ->
                            handleError(error)
                            mDisposable.clear()
                        }
                )
        )
    }

    private fun showMessage() {

    }

    private fun handleError(@NonNull throwable: Throwable) {
        mProgressEvent.postValue(false)
    }
}