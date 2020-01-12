package com.igor.langugecards.presentation.view.custom.observer

interface LanguageCardGestureListener {

    fun onScrollUp()

    fun onScrollDown()

    fun onFlip(flipped : Boolean)
}