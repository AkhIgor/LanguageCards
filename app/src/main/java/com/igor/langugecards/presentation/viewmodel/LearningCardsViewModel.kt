package com.igor.langugecards.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor.langugecards.database.room.DAO.CardInteractor
import com.igor.langugecards.model.Card
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class LearningCardsViewModel(
        val cardInteractor: CardInteractor,
        val disposable: CompositeDisposable) : ViewModel() {

    private val cardList: MutableList<Card> = ArrayList()
    val card = MutableLiveData<Card>()
    val flipped: LiveData<Boolean> = MutableLiveData<Boolean>()
    val progress = MutableLiveData<Boolean>()

    private fun readCardsFromDb() {
        disposable.add(
                cardInteractor.getAllCards()
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe { progress.postValue(true) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate { progress.postValue(false) }
                        .map { list -> mixCards(list) }
                        .subscribe({ resultList -> cardList.addAll(resultList) },
                                { error -> showError(error.message) }
                        )
        )
    }

    /**
     * Миксует элементы следующим образом:
     * проходит по каждому элементу списка, и на кажждой из итераций
     * генерирует случайный индекс элемента -> меняет значения текущего
     * и сгенерированного элементов местами
     */
    private fun mixCards(cardList: MutableList<Card>): List<Card> {
        val random = Random()

        var randomIndex: Int
        var randomElement: Card
        var currElement: Card

        for (currIndex in cardList.indices) {
            randomIndex = random.nextInt(cardList.size)
            randomElement = cardList[randomIndex]
            currElement = cardList[currIndex]
            cardList[currIndex] = randomElement
            cardList[randomIndex] = currElement
        }

        return cardList
    }

    private fun showError(errorMessage: String?) {

    }
}
