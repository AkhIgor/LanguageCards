package com.igor.langugecards.presentation.view.custom.binding

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.igor.langugecards.model.Card
import com.igor.langugecards.presentation.view.custom.LanguageCardView

@BindingAdapter("card")
fun setCard(view: LanguageCardView, card: MutableLiveData<Card>) {
    if (card.value != null) {
        with(card.value!!) {
            view.cardTheme = theme
            view.cardLanguage = fromLanguage
            view.cardNativeWord = nativeWord
            view.cardTranslatedWord = translatedWord
        }
    }

    if (!card.value?.theme.isNullOrBlank()) {
        view.updateThemeLength()
    }
}