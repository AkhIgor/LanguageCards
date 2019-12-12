package com.igor.langugecards.presentation.gestures

import kotlin.math.abs

class Gesture {

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UNDEFINED
    }

    companion object {
        private const val SWIPE_MIN_DISTANCE = 50f
        private const val SWIPE_MAX_OFF_PATH = 200f

        fun getDirection(distanceX: Float, distanceY: Float): Direction {
            val displacementX = abs(distanceX)
            val displacementY = abs(distanceY)

            return if ((SWIPE_MIN_DISTANCE..SWIPE_MAX_OFF_PATH).contains(displacementX)
                    && (SWIPE_MIN_DISTANCE..SWIPE_MAX_OFF_PATH).contains(displacementY)) {
                if (displacementX > displacementY) {
                    if (distanceX > 0) Direction.LEFT else Direction.RIGHT
                } else {
                    if (distanceY > 0) Direction.UP else Direction.DOWN
                }
            } else if ((SWIPE_MIN_DISTANCE..SWIPE_MAX_OFF_PATH).contains(displacementX)) {
                if (distanceX > 0) Direction.LEFT else Direction.RIGHT
            } else if ((SWIPE_MIN_DISTANCE..SWIPE_MAX_OFF_PATH).contains(displacementY)) {
                if (distanceY > 0) Direction.UP else Direction.DOWN
            } else {
                Direction.UNDEFINED
            }
        }
    }
}