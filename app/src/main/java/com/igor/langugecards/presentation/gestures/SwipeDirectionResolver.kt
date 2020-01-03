package com.igor.langugecards.presentation.gestures

import android.util.Log
import android.view.MotionEvent
import kotlin.math.abs

class SwipeDirectionResolver {

    private companion object {
        private const val SWIPE_MIN_DISTANCE = 50f
        private const val SWIPE_MAX_OFF_PATH = 200f
    }

    /**
     * Функция-распознователь направления свайпа
     */
    fun resolveSwipeDirection(firstEvent: MotionEvent?, secondEvent: MotionEvent?): SwipeDirection {

        if (touchEventsNotExist(firstEvent, secondEvent)) {
            return SwipeDirection.UNDEFINED
        }

        val distanceX = computeDelta(firstEvent!!.x, secondEvent!!.x)
        val distanceY = computeDelta(firstEvent.y, secondEvent.y)

        val displacementX = abs(distanceX)
        val displacementY = abs(distanceY)

        Log.d("Direction", "X: $distanceX")
        Log.d("Direction", "Y: $distanceY")

        return if ((SWIPE_MIN_DISTANCE..SWIPE_MAX_OFF_PATH).contains(displacementX)
                && (SWIPE_MIN_DISTANCE..SWIPE_MAX_OFF_PATH).contains(displacementY)) {
            if (displacementX > displacementY) {
                if (distanceX > 0) SwipeDirection.LEFT else SwipeDirection.RIGHT
            } else {
                if (distanceY > 0) SwipeDirection.UP else SwipeDirection.DOWN
            }
        } else if ((SWIPE_MIN_DISTANCE..SWIPE_MAX_OFF_PATH).contains(displacementX)) {
            if (distanceX > 0) SwipeDirection.LEFT else SwipeDirection.RIGHT
        } else if ((SWIPE_MIN_DISTANCE..SWIPE_MAX_OFF_PATH).contains(displacementY)) {
            if (distanceY > 0) SwipeDirection.UP else SwipeDirection.DOWN
        } else {
            SwipeDirection.UNDEFINED
        }
    }

    private fun computeDelta(firstPoint: Float, secondPoint: Float) = firstPoint - secondPoint

    private fun touchEventsNotExist(firstEvent: MotionEvent?, secondEvent: MotionEvent?): Boolean {
        return (firstEvent == null || secondEvent == null)
    }
}