package com.igor.langugecards.presentation.viewmodel

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor.langugecards.R
import com.igor.langugecards.database.room.DAO.CardInteractor
import com.igor.langugecards.model.Card
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CardListViewModel(
    private val cardInteractor: CardInteractor
) : ViewModel() {

    val progressEvent = MutableLiveData<Boolean>()
    val listIsEmpty = MutableLiveData<Boolean>(false)
    val cards = MutableLiveData<MutableList<Card>>()
    val operationStatusEvent = MutableLiveData<Int>()
    private val disposable = CompositeDisposable()

    fun onScreenStart() {
        loadCards()
    }

    fun removeCard(cardPosition: Int) {
        if (!cards.value.isNullOrEmpty()) {
            val card = cards.value!![cardPosition]
            disposable.add(
                Completable.fromAction { cardInteractor.deleteCard(card) }
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            onSuccessRemove(cardPosition)
                            operationStatusEvent.postValue(R.string.successful_delete_operation)
                        },
                        { handleError(it) }
                    )
            )
        }
    }

    private fun loadCards() {
        disposable.add(cardInteractor.getAllCards()
            .doOnSubscribe { progressEvent.postValue(true) }
            .subscribe(
                { cardList ->
                    if (cardList.isNullOrEmpty()) {
                        listIsEmpty.postValue(true)
                    } else {
                        cards.postValue(cardList)
                    }
                    progressEvent.postValue(false)
                    disposable.clear()
                },
                { error ->
                    handleError(error)
                    disposable.clear()
                }
            )
        )
    }

    private fun onSuccessRemove(cardPosition: Int) {
        showMessage()
        cards.value!!.removeAt(cardPosition)
        if (cards.value.isNullOrEmpty()) {
            listIsEmpty.postValue(true)
        }
    }

    private fun handleError(@NonNull throwable: Throwable) {
        Log.d("CardListViewModel", throwable.message)
        progressEvent.postValue(false)
        operationStatusEvent.postValue(R.string.error_occured)
    }

    private fun showMessage() {
    }
}