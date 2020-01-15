package com.igor.langugecards.presentation.view.custom

import android.animation.Animator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toRectF
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.igor.langugecards.R
import com.igor.langugecards.constants.Constants.NO_COLOR
import com.igor.langugecards.presentation.gestures.GestureListener
import com.igor.langugecards.presentation.gestures.SwipeDirection
import com.igor.langugecards.presentation.view.custom.extensions.dpToPx
import com.igor.langugecards.presentation.view.custom.observer.LanguageCardGestureListener

class LanguageCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), Animator.AnimatorListener {

    companion object {
        private const val FLIPPING_ANIMATION_DURATION: Long = 440L
        private const val SCROLLING_ANIMATION_DURATION: Long = 700L
        private const val APPEARANCE_ANIMATION_DURATION: Long = 300L

        private const val SWIPE_TO_RIGHT_DEGREE = 90f
        private const val SWIPE_TO_LEFT_DEGREE = -90f

        private const val DEFAULT_SIZE = 40
        private const val CAMERA_DISTANCE = 8000
    }

    private var requiredTopPosition = 0f

    private var requiredBottomPosition = 0f

    private var flipped: Boolean = false

    private var animationIsRunning: Boolean = false

    private var scrollAnimation: Boolean = false

    private var flipAnimation: Boolean = false

    private var gestureListener = GestureListener(context, this)

    private var flipRTL: Boolean = false

    private var flipLTR: Boolean = false

    private var scrollUp: Boolean = false

    private var scrollDown: Boolean = false

    private var cornerRadius: Float = 0f

    private var cardTintColor: Int = NO_COLOR

    private val maskPaint: Paint = Paint(ANTI_ALIAS_FLAG)

    private val viewRect = Rect().toRectF()

    private lateinit var gestureManagerListener: LanguageCardGestureListener

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LanguageCardView)
            cornerRadius = typedArray.getDimension(
                R.styleable.LanguageCardView_corner_radius,
                context.dpToPx(cornerRadius.toInt())
            )

            cardTintColor = typedArray.getColor(
                R.styleable.LanguageCardView_card_tint_color,
                NO_COLOR
            )

            typedArray.recycle()
        }

        animate().setListener(this)
            .interpolator = FastOutSlowInInterpolator()

        if (cardTintColor != NO_COLOR) {
            setupPaint()
        }

        cameraDistance = context.dpToPx(CAMERA_DISTANCE)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val initWidth = resolveDefaultSize(widthMeasureSpec)
        val initHeight = resolveDefaultSize(heightMeasureSpec)
        setMeasuredDimension(initWidth, initHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        with(viewRect) {
            left = 0f
            top = 0f
            right = w.toFloat()
            bottom = h.toFloat()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        requiredTopPosition = top.toFloat()
        requiredBottomPosition = bottom.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(viewRect, cornerRadius, cornerRadius, maskPaint)
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
                animate().setDuration(FLIPPING_ANIMATION_DURATION)
                    .rotationYBy(SWIPE_TO_LEFT_DEGREE)
                    .start()
                flipAnimation = true
                flipRTL = true
                true
            }
            SwipeDirection.RIGHT -> {
                animate().setDuration(FLIPPING_ANIMATION_DURATION)
                    .rotationYBy(SWIPE_TO_RIGHT_DEGREE)
                    .start()
                flipAnimation = true
                flipLTR = true
                true
            }
            SwipeDirection.UP -> {
                animate()
                    .yBy(-requiredBottomPosition)
                    .setDuration(SCROLLING_ANIMATION_DURATION)
                    .start()
                scrollAnimation = true
                scrollUp = true
                true
            }
            SwipeDirection.DOWN -> {
                animate()
                    .yBy(requiredBottomPosition)
                    .setDuration(SCROLLING_ANIMATION_DURATION)
                    .start()
                scrollAnimation = true
                scrollDown = true
                true
            }
            else -> false
        }
    }

    override fun performClick(): Boolean {
        super.performClick()

        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureListener.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
            performClick()
        }
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        animationIsRunning = false
        if (scrollAnimation) {
            alpha = 0f
            y = requiredTopPosition
            if (scrollUp) {
                scrollUp = false
                gestureManagerListener.onScrollUp()
            } else if (scrollDown) {
                scrollDown = false
                gestureManagerListener.onScrollDown()
            }
            scrollAnimation = false
            animate().setDuration(APPEARANCE_ANIMATION_DURATION)
                .alpha(1f)
                .start()
        } else if (flipAnimation) {
            if (flipLTR) {
                rotationY = SWIPE_TO_LEFT_DEGREE
                flipLTR = false
                animate().setDuration(FLIPPING_ANIMATION_DURATION)
                    .rotationYBy(SWIPE_TO_RIGHT_DEGREE)
                    .start()
            } else if (flipRTL) {
                rotationY = SWIPE_TO_RIGHT_DEGREE
                flipRTL = false
                animate().setDuration(FLIPPING_ANIMATION_DURATION)
                    .rotationYBy(SWIPE_TO_LEFT_DEGREE)
                    .start()
            }
            flipAnimation = false
            flipped = !flipped
            gestureManagerListener.onFlip(flipped)
        }
    }

    override fun onAnimationCancel(animation: Animator?) {
        animationIsRunning = false
        scrollAnimation = false
    }

    override fun onAnimationStart(animation: Animator?) {
        animationIsRunning = true
    }

    private fun resolveDefaultSize(spec: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SIZE).toInt()
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(spec)
            MeasureSpec.AT_MOST -> MeasureSpec.getSize(spec)
            else -> MeasureSpec.getSize(spec)

        }
    }

    private fun setupPaint() {
        background = context.getDrawable(R.drawable.empty_view)

        with(maskPaint) {
            color = cardTintColor
            style = Paint.Style.FILL
        }
    }
}