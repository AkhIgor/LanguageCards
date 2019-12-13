package com.igor.langugecards.presentation.view.custom.observer

interface LanguageCardViewListener {

    fun onFlipped(flipped: Boolean)

    fun onScrollUp()

    fun onScrollDown()
}