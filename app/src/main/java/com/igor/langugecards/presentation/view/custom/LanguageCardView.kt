package com.igor.langugecards.presentation.view.custom

import android.animation.Animator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
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
        private const val FLIPPING_ANIMATION_DURATION: Long = 500L
        private const val SCROLLING_ANIMATION_DURATION: Long = 700L
        private const val APPEARANCE_ANIMATION_DURATION: Long = 300L

        private const val SWIPE_TO_RIGHT_DEGREE = 90f
        private const val SWIPE_TO_LEFT_DEGREE = -90f
    }

    private var requiredTopPosition = 0f

    private var requiredBottomPosition = 0f

    // private val flipAnimator = animate()
    //
    // private val scrollAnimator = animate()
    //
    // private val appearanceAnimator = animate()

    private var flipped: Boolean = false

    private var animationIsRunning: Boolean = false

    private var scrollAnimation: Boolean = false

    private var flipAnimation: Boolean = false

    private var gestureListener = GestureListener(context, this)

    private var flipRTL: Boolean = false

    private var flipLTR: Boolean = false

    private var scrollUp: Boolean = false

    private var scrollDown: Boolean = false

    private lateinit var maskBitmap: Bitmap
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val maskPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private var cornerRadius = 0f

    private val CORNER_RADIUS = context.dpToPx(20)

    private lateinit var gestureManagerListener: LanguageCardGestureListener

    init {
        animate().setListener(this)
                .interpolator = FastOutSlowInInterpolator()

        val metrics = context.resources.displayMetrics
        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_RADIUS, metrics)

        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        setWillNotDraw(false)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val offscreenBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val offscreenCanvas = Canvas(offscreenBitmap)
        super.draw(offscreenCanvas)
        maskBitmap = createMask(width, height)
        offscreenCanvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint)
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        requiredTopPosition = top.toFloat()
        requiredBottomPosition = bottom.toFloat()
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

        val distance = 80000
        val scale = resources.displayMetrics.density * distance

        return when (direction) {
            SwipeDirection.LEFT -> {
                animate().setDuration(FLIPPING_ANIMATION_DURATION)
                        .rotationYBy(SWIPE_TO_LEFT_DEGREE)
                        .start()
                flipAnimation = true
                flipRTL = true
                cameraDistance = scale
                true
            }
            SwipeDirection.RIGHT -> {
                animate().setDuration(FLIPPING_ANIMATION_DURATION)
                        .rotationYBy(SWIPE_TO_RIGHT_DEGREE)
                        .start()
                flipAnimation = true
                flipLTR = true
                cameraDistance = scale
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

    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        animationIsRunning = false
        if (scrollAnimation) {
            alpha = 0f
            y = requiredTopPosition      //300
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

    private fun createMask(width: Int, height: Int): Bitmap {
        val mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        val canvas = Canvas(mask)
        val paint = Paint(ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        canvas.drawRect(x, y, width.toFloat(), height.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRoundRect(RectF(x, y, width.toFloat(), height.toFloat()), cornerRadius, cornerRadius, paint)
        return mask
    }
}