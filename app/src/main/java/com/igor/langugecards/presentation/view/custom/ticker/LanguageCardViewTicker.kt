package com.igor.langugecards.presentation.view.custom.ticker

import java.lang.Thread.sleep

class LanguageCardViewTicker(
        private val consumer: TickerView
) : Runnable {

    private companion object {
        const val SPACE_BETWEEN_CYCLE = "       "
        const val UPDATING_TIME = 10L
        const val UPDATING_SHIFT = 3f
    }

    private var animate = false

    override fun run() {
        while (animate) {
            sleep(UPDATING_TIME)
            consumer.updateTicker()
        }
    }

    fun getTickeredString(baseString: String): String {
        return baseString + SPACE_BETWEEN_CYCLE
    }

    fun updateCursorPosition(boundaryLeft: Int, boundaryRight: Int, currentPositionX: Float, textLength: Float): Float {
        return if ((boundaryLeft.toFloat() - textLength/2) <= currentPositionX) {
            currentPositionX - UPDATING_SHIFT
        } else {
            boundaryRight.toFloat() + textLength / 2
        }
    }

    fun startThread() {
        if (!animate) {
            animate = true
            Thread(this)
                    .start()
        }
    }

    fun stopThread() {
        animate = false
    }
}