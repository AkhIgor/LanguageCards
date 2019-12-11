package com.igor.langugecards.presentation.gestures

import kotlin.math.abs

class Gesture {

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    companion object {
        fun getDirection(distanceX: Float, distanceY: Float): Direction {
            val displacementX = abs(distanceX)
            val displacementY = abs(distanceY)

            return if (displacementX > displacementY) {
                if (distanceX > 0) Direction.LEFT else Direction.RIGHT
            } else {
                if (distanceY > 0) Direction.DOWN else Direction.UP
            }
        }
    }
}