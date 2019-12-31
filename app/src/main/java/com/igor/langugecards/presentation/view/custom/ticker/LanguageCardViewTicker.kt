package com.igor.langugecards.presentation.view.custom.ticker

import java.lang.Thread.sleep

class LanguageCardViewTicker(
        private val consumer: TickerView
) : Runnable {

    private companion object {
        const val SPACE_BETWEEN_CYCLE = "   "
        const val UPDATING_TIME = 10L
        const val UPDATING_SHIFT = 3f
    }

    var animate = true

    override fun run() {
        while (animate) {
            sleep(UPDATING_TIME)
            consumer.updateTicker()
        }
    }

    fun getTickeredString(baseString: String): String {
        val firstSubstring = baseString.substring(1, baseString.lastIndex + 1)
        val secondSubstring = baseString[0]
        return firstSubstring + SPACE_BETWEEN_CYCLE + secondSubstring
    }

    fun updateCursorPosition(boundaryLeft: Int, boundaryRight: Int, currentPositionX: Float): Float {
        return if (currentPositionX <= boundaryLeft) {
            boundaryRight.toFloat()
        } else {
            currentPositionX - UPDATING_SHIFT
        }
    }

    fun startThread() {
        Thread(this)
                .start()
    }
}