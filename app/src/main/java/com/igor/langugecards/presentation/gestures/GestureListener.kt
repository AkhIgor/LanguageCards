package com.igor.langugecards.presentation.gestures

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.igor.langugecards.presentation.view.custom.LanguageCardView
import kotlin.math.abs

class GestureListener(val context: Context,
                      val cardView: LanguageCardView) : GestureDetector.OnGestureListener {

    private val detector: GestureDetectorCompat = GestureDetectorCompat(context, this)

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UNDEFINED
    }

    private companion object {
        private const val SWIPE_MIN_DISTANCE = 50f
        private const val SWIPE_MAX_OFF_PATH = 200f
    }

    /**
     * Функция-распознователь направления свайпа
     */
    fun getSwipeDirection(firstEvent: MotionEvent?, secondEvent: MotionEvent?, flipped: Boolean): Direction {

        if (touchEventsNotExist(firstEvent, secondEvent)) {
            return Direction.UNDEFINED
        }

        val distanceX = computeDelta(flipped, firstEvent!!.x, secondEvent!!.x)
        val distanceY = computeDelta(firstEvent.y, secondEvent.y)

        val displacementX = abs(distanceX)
        val displacementY = abs(distanceY)

        Log.d("Direction", "X: $distanceX")
        Log.d("Direction", "Y: $distanceY")

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

    fun onTouchEvent(event: MotionEvent): Boolean {
        return detector.onTouchEvent(event)
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.d("CardView", "onShowPress")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.d("CardView", "onSingleTapUp")

        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Log.d("CardView", "onDown")

        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        Log.d("CardView", "onFling")

        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        Log.d("CardView", "onLongPress")
    }

    private fun computeDelta(flipped: Boolean, firstPoint: Float, secondPoint: Float): Float {
        return if (flipped) {
            firstPoint - secondPoint
        } else {
            secondPoint - firstPoint
        }
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return cardView.onScroll(e1, e2, distanceX, distanceY)
    }

    private fun computeDelta(firstPoint: Float, secondPoint: Float) = firstPoint - secondPoint

    private fun touchEventsNotExist(firstEvent: MotionEvent?, secondEvent: MotionEvent?): Boolean {
        return (firstEvent == null || secondEvent == null)
    }
}
