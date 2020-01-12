package com.igor.langugecards.presentation.view.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.igor.langugecards.presentation.gestures.GestureListener
import com.igor.langugecards.presentation.gestures.SwipeDirection
import com.igor.langugecards.presentation.view.custom.observer.LanguageCardGestureListener

class LanguageCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    companion object {
        private const val FLIPPING_ANIMATION_DURATION: Long = 500
        private const val SCROLLING_ANIMATION_DURATION: Long = 700
        private const val APPEARANCE_ANIMATION_DURATION: Long = 300

        private const val SWIPE_TO_RIGHT_DEGREE = 90f
        private const val SWIPE_TO_LEFT_DEGREE = -90f
    }

    private var requiredTopPosition = 0f

    private var requiredBottomPosition = 0f

    private val flipAnimator = animate()

    private val scrollAnimator = animate()

    private val appearanceAnimator = animate()

    private var flipped: Boolean = false

    private var animationIsRunning: Boolean = false

    private var scrollAnimation: Boolean = false

    private var flipAnimation: Boolean = false

    private var gestureListener = GestureListener(context, this)

    private var flipRTL: Boolean = false

    private var flipLTR: Boolean = false

    private lateinit var gestureManagerListener: LanguageCardGestureListener

    init {
        flipAnimator.duration = FLIPPING_ANIMATION_DURATION
            // .setListener(this)

        scrollAnimator
            .setDuration(SCROLLING_ANIMATION_DURATION)
            // .setListener(this)
            .interpolator = FastOutSlowInInterpolator()

        appearanceAnimator
            .setDuration(APPEARANCE_ANIMATION_DURATION)
            .interpolator = FastOutSlowInInterpolator()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        requiredTopPosition = top.toFloat()
        requiredBottomPosition = bottom.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)     //1206, 828
        Log.e("CardView", "onDraw")

        Log.d("CardViewPosition", "$top")
        Log.d("CardViewPosition", "$y")
    }

    /**
     * Установить слушателя событий, для уведомления о изменений состояния View
     *
     * @param listener слушатель событий
     */
    fun setViewListener(listener: LanguageCardGestureListener) {
        gestureManagerListener = listener
    }

    fun onScroll(firstMotionEvent: MotionEvent?, secondMotionEvent: MotionEvent?): Boolean {
        Log.d("CardView", "onScroll")

        if (animationIsRunning) {
            return false
        }

        val direction = gestureListener.getSwipeDirection(firstMotionEvent, secondMotionEvent)

        Log.d("Direction", "Direction: $direction")

        return when (direction) {
            SwipeDirection.LEFT -> {
                // flipAnimator.rotationYBy(SWIPE_TO_LEFT_DEGREE)
                flipAnimation = true
                flipRTL = true
                flipped = !flipped
                flipAnimator.start()
                gestureManagerListener.onFlip(flipped)
                true
            }
            SwipeDirection.RIGHT -> {
                flipAnimator.yBy(-requiredBottomPosition)
                flipAnimation = true
                flipLTR = true
                flipped = !flipped
                flipAnimator.start()
                gestureManagerListener.onFlip(flipped)
                true
            }
            SwipeDirection.UP -> {
                scrollAnimator.yBy(-requiredBottomPosition)
                scrollAnimation = true
                scrollAnimator.start()
                gestureManagerListener.onScrollUp()
                true
            }
            SwipeDirection.DOWN -> {
                scrollAnimator.yBy(requiredBottomPosition)
                scrollAnimation = true
                scrollAnimator.start()
                gestureManagerListener.onScrollDown()
                true
            }
            else -> false
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        launchMissile()

        return true
    }

    private fun launchMissile() {
        Toast.makeText(context, "Missile launched", Toast.LENGTH_SHORT).show()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureListener.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
            performClick()
        }
    }
    //
    // override fun onAnimationRepeat(animation: Animator?) {
    //
    // }
    //
    // override fun onAnimationEnd(animation: Animator?) {
    //     animationIsRunning = false
    //     if (scrollAnimation) {
    //         alpha = 0f
    //         y = requiredTopPosition      //300
    //         appearanceAnimator.alpha(1f)
    //         appearanceAnimator.start()
    //         scrollAnimation = false
    //     } else if (flipAnimation) {
    //         if (flipLTR) {
    //             rotationY = SWIPE_TO_LEFT_DEGREE
    //             flipAnimator.rotationYBy(SWIPE_TO_RIGHT_DEGREE)
    //             flipLTR = false
    //             flipAnimator.start()
    //             flipAnimation = false
    //         } else if (flipRTL) {
    //             rotationY = SWIPE_TO_RIGHT_DEGREE
    //             flipAnimator.rotationYBy(SWIPE_TO_LEFT_DEGREE)
    //             flipRTL = false
    //             flipAnimator.start()
    //             flipAnimation = false
    //         }
    //     }
    // }
    //
    // override fun onAnimationCancel(animation: Animator?) {
    //     animationIsRunning = false
    //     scrollAnimation = false
    // }
    //
    // override fun onAnimationStart(animation: Animator?) {
    //     animationIsRunning = true
    // }
}