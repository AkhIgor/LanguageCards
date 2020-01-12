package com.igor.langugecards.presentation.view.custom

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.cardview.widget.CardView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.igor.langugecards.R
import com.igor.langugecards.presentation.gestures.GestureListener
import com.igor.langugecards.presentation.gestures.SwipeDirection
import com.igor.langugecards.presentation.view.custom.extensions.dpToPx
import com.igor.langugecards.presentation.view.custom.observer.LanguageCardGestureListener
import com.igor.langugecards.presentation.view.custom.ticker.LanguageCardViewTicker
import com.igor.langugecards.presentation.view.custom.ticker.TickerView

class LanguageCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr), Animator.AnimatorListener {

    companion object {
        private const val DEFAULT_LOAD_TINT_COLOR = R.color.colorShimmer
        private const val DEFAULT_SIZE = 40

        private const val SIDE_MARGINS = 32

        private const val DEFAULT_THEME_TEXT_SIZE = 28
        private const val DEFAULT_LANGUAGE_TEXT_SIZE = 18
        private const val DEFAULT_WORD_TEXT_SIZE = 24

        private const val THEME_TEXT_MARGIN_TOP = 16
        private const val THEME_LANGUAGE_MARGIN_TOP = 48
        private const val THEME_WORD_MARGIN_TOP = 120

        private const val FLIPPING_ANIMATION_DURATION: Long = 500
        private const val SCROLLING_ANIMATION_DURATION: Long = 700
        private const val APPEARANCE_ANIMATION_DURATION: Long = 300

        private const val SWIPE_TO_RIGHT_DEGREE = 90f
        private const val SWIPE_TO_LEFT_DEGREE = -90f
    }

    private var requiredTopPosition = 0f
    private var requiredBottomPosition = 0f

    private val paintBrush = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()

    private var cardThemeAnimate: Boolean = false

    private var centerX: Float = 0f

    private var themeCursorY: Float = 0f

    private var themeCursorX: Float = 0f

    private var languageCursorY: Float = 0f

    private var wordCursorY: Float = 0f

    private var flipped: Boolean = false

    private val flipAnimator = animate()

    private val scrollAnimator = animate()

    private val appearanceAnimator = animate()

    private var animationIsRunning: Boolean = false

    private var scrollAnimation: Boolean = false

    private var flipAnimation: Boolean = false

    private var gestureListener = GestureListener(context, this)

    private var flipRTL: Boolean = false

    private var flipLTR: Boolean = false

    var themeLengthSize = 0f

    private lateinit var mGestureListener: LanguageCardGestureListener

    init {
        flipAnimator
            .setDuration(FLIPPING_ANIMATION_DURATION)
            .setListener(this)
            .interpolator = FastOutSlowInInterpolator()

        scrollAnimator
            .setDuration(SCROLLING_ANIMATION_DURATION)
            .setListener(this)
            .interpolator = FastOutSlowInInterpolator()

        appearanceAnimator
            .setDuration(APPEARANCE_ANIMATION_DURATION)
            .interpolator = FastOutSlowInInterpolator()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (w == 0) return
        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }
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
        mGestureListener = listener
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
                flipAnimator.rotationYBy(SWIPE_TO_LEFT_DEGREE)
                flipAnimation = true
                flipRTL = true
                flipped = !flipped
                flipAnimator.start()
                true
            }
            SwipeDirection.RIGHT -> {
                flipAnimator.rotationYBy(SWIPE_TO_RIGHT_DEGREE)
                flipAnimation = true
                flipLTR = true
                flipped = !flipped
                flipAnimator.start()
                true
            }
            SwipeDirection.UP -> {
                scrollAnimator.yBy(-requiredBottomPosition)
                scrollAnimation = true
                scrollAnimator.start()
                mGestureListener.onScrollUp()
                true
            }
            SwipeDirection.DOWN -> {
                scrollAnimator.yBy(requiredBottomPosition)
                scrollAnimation = true
                scrollAnimator.start()
                mGestureListener.onScrollDown()
                true
            }
            else -> false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureListener.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onAnimationRepeat(animation: Animator?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationEnd(animation: Animator?) {
        animationIsRunning = false
        if (scrollAnimation) {
            alpha = 0f
            y = requiredTopPosition      //300
            appearanceAnimator.alpha(1f)
            appearanceAnimator.start()
            scrollAnimation = false
        } else if (flipAnimation) {
            if (flipLTR) {
                rotationY = SWIPE_TO_LEFT_DEGREE
                flipAnimator.rotationYBy(SWIPE_TO_RIGHT_DEGREE)
                flipLTR = false
                flipAnimator.start()
            } else if (flipRTL) {
                rotationY = SWIPE_TO_RIGHT_DEGREE
                flipAnimator.rotationYBy(SWIPE_TO_LEFT_DEGREE)
                flipRTL = false
                flipAnimator.start()
            }
        }
    }

    override fun onAnimationCancel(animation: Animator?) {
        animationIsRunning = false
        scrollAnimation = false
    }

    override fun onAnimationStart(animation: Animator?) {
        animationIsRunning = true
    }
}