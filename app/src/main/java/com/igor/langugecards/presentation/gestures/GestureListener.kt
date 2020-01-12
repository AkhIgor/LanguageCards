package com.igor.langugecards.presentation.gestures

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.igor.langugecards.presentation.view.custom.LanguageCardView

class GestureListener(
    context: Context,
    private val cardView: LanguageCardView
) : GestureDetector.OnGestureListener {

    private val swipeDirectionResolver: SwipeDirectionResolver = SwipeDirectionResolver()
    private val detector: GestureDetectorCompat = GestureDetectorCompat(context, this)

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

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return cardView.onScroll(e1, e2)
    }

    fun getSwipeDirection(e1: MotionEvent?, e2: MotionEvent?) = swipeDirectionResolver.resolveSwipeDirection(e1, e2)
}
