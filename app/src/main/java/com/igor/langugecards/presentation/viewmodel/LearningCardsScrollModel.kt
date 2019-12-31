package com.igor.langugecards.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.igor.langugecards.database.room.DAO.CardInteractor
import com.igor.langugecards.model.Card
import com.igor.langugecards.presentation.view.custom.observer.LanguageCardScrollListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class LearningCardsScrollModel(
        val cardInteractor: CardInteractor,
        val disposable: CompositeDisposable) : ViewModel(), LanguageCardScrollListener {

    private val cardList: MutableList<Card> = ArrayList()
    var card = MutableLiveData<Card>()
    val progress = MutableLiveData<Boolean>()

    private var currentCardPosition: Int = 0

    fun onScreenStart() {
        readCardsFromDb()
    }

    override fun onScrollUp() {
        currentCardPosition++
        card.postValue(cardList[currentCardPosition])
    }

    override fun onScrollDown() {
        currentCardPosition--
        card.postValue(cardList[currentCardPosition])
    }

    private fun readCardsFromDb() {
        disposable.add(
                cardInteractor.getAllCards()
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe { progress.postValue(true) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate { progress.postValue(false) }
                        .map { list -> mixCards(list) }
                        .subscribe(
                                { initCards(it) },
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

    private fun initCards(cards: List<Card>) {
        if (cards.isNotEmpty()) {
            cardList.addAll(cards)
            card.postValue(cards[currentCardPosition])
        } else {
            showError("?!")
        }
    }

    private fun showError(errorMessage: String?) {

    }
}
