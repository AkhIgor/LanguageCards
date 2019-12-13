package com.igor.langugecards.presentation.view.custom

import android.animation.Animator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.igor.langugecards.R
import com.igor.langugecards.presentation.gestures.GestureListener
import com.igor.langugecards.presentation.view.custom.extensions.dpToPx
import com.igor.langugecards.presentation.view.custom.extensions.spToPx
import com.igor.langugecards.presentation.view.custom.observer.LanguageCardViewListener


class LanguageCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr), Animator.AnimatorListener {

    companion object {
        private const val DEFAULT_LOAD_TINT_COLOR = R.color.colorShimmer
        private const val DEFAULT_SIZE = 40

        private const val SIDE_MARGIN = 16

        private const val DEFAULT_THEME_TEXT_SIZE = 28
        private const val DEFAULT_LANGUAGE_TEXT_SIZE = 18
        private const val DEFAULT_WORD_TEXT_SIZE = 24

        private const val THEME_TEXT_MARGIN_TOP = 16
        private const val THEME_LANGUAGE_MARGIN_TOP = 48
        private const val THEME_WORD_MARGIN_TOP = 120

        private const val FLIPPING_ANIMATION_DURATION: Long = 500
        private const val SCROLLING_ANIMATION_DURATION: Long = 700
        private const val APPEARANCE_ANIMATION_DURATION: Long = 300

        private const val SWIPE_TO_RIGHT_DEGREE = -180f
        private const val SWIPE_TO_LEFT_DEGREE = 180f
    }

    @ColorRes
    private var loadTintColor = DEFAULT_LOAD_TINT_COLOR
    private var cardTheme: String = context.getString(R.string.not_defined)
    private var cardLanguage: String = context.getString(R.string.not_defined)
    private var cardWord: String = context.getString(R.string.not_defined)
    private var cardThemeTextSize: Float = DEFAULT_THEME_TEXT_SIZE.toFloat()
    private var cardLanguageTextSize = DEFAULT_LANGUAGE_TEXT_SIZE.toFloat()
    private var cardWordTextSize = DEFAULT_WORD_TEXT_SIZE.toFloat()

    private var requiredTopPosition = 0f
    private var requiredBottomPosition = 0f

    private val paintBrush = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()

    private var cardThemeAnimate: Boolean = false

    private var centerX: Float = 0f

    private var themeCursorY: Float = 0f

    private var languageCursorY: Float = 0f

    private var wordCursorY: Float = 0f

    private var flipped: Boolean = false

    private val flipAnimator = animate()

    private val scrollAnimator = animate()

    private val appearanceAnimator = animate()

    private var animationIsRunning: Boolean = false

    private var scrollAnimation: Boolean = false

    private lateinit var gestureListener: GestureListener

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LanguageCardView)
            loadTintColor = typedArray.getResourceId(
                    R.styleable.LanguageCardView_card_load_tint, DEFAULT_LOAD_TINT_COLOR)

            cardTheme = typedArray.getString(R.styleable.LanguageCardView_card_theme)
                    ?: context.getString(R.string.not_defined)

            cardThemeTextSize = typedArray.getDimension(R.styleable.LanguageCardView_card_theme_text_size,
                    context.spToPx(DEFAULT_THEME_TEXT_SIZE))

            cardLanguage = typedArray.getString(R.styleable.LanguageCardView_card_language)
                    ?: context.getString(R.string.not_defined)

            cardLanguageTextSize = typedArray.getDimension(R.styleable.LanguageCardView_card_language_text_size,
                    context.spToPx(DEFAULT_LANGUAGE_TEXT_SIZE))

            cardWord = typedArray.getString(R.styleable.LanguageCardView_card_word)
                    ?: context.getString(R.string.not_defined)

            cardWordTextSize = typedArray.getDimension(R.styleable.LanguageCardView_card_word_text_size,
                    context.spToPx(DEFAULT_WORD_TEXT_SIZE))

            typedArray.recycle()

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

            gestureListener = GestureListener(context, this)
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val initialWidth = resolveDefaultSize(widthMeasureSpec)
        val initialHeight = resolveDefaultSize(heightMeasureSpec)
        setMeasuredDimension(initialWidth, initialHeight)

        centerX = (initialWidth / 2).toFloat()

        themeCursorY = context.dpToPx(THEME_TEXT_MARGIN_TOP) + cardThemeTextSize
        languageCursorY = themeCursorY + context.dpToPx(THEME_LANGUAGE_MARGIN_TOP) + cardLanguageTextSize
        wordCursorY = languageCursorY + context.dpToPx(THEME_WORD_MARGIN_TOP) + cardWordTextSize

        val themeLengthSize = cardThemeTextSize / 2 * cardTheme.length
        val availableWidthSpace = widthMeasureSpec - (2 * context.dpToPx(SIDE_MARGIN))
        if (themeLengthSize >= availableWidthSpace) {
            cardThemeAnimate = true
        }
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

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        Log.d("CardViewPosition", "draw $top")
        Log.d("CardViewPosition", "draw $y")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)     //1206, 828
        Log.e("CardView", "onDraw")

        paintBrush.textSize = cardThemeTextSize
        paintBrush.textAlign = Paint.Align.CENTER

        setup(Color.RED, Paint.Style.FILL)
        canvas.drawText(cardTheme, centerX, themeCursorY, paintBrush)

        setup(Color.GRAY, Paint.Style.FILL)
        paintBrush.textSize = cardLanguageTextSize
        canvas.drawText(cardLanguage, centerX, languageCursorY, paintBrush)

        setup(Color.BLACK, Paint.Style.FILL)
        paintBrush.textSize = cardWordTextSize
        canvas.drawText(cardWord, centerX, wordCursorY, paintBrush)

        Log.d("CardViewPosition", "$top")
        Log.d("CardViewPosition", "$y")
    }

    /**
     * Установить слушателя событий, для уведомления о изменений состояния View
     *
     * @param listener слушатель событий
     */
    fun setScrollListener(listener: LanguageCardViewListener) {

    }

    fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        Log.d("CardView", "onScroll")

        if (animationIsRunning) {
            return false
        }

        val direction = gestureListener.getSwipeDirection(e1, e2, flipped)

        Log.d("Direction", "Direction: $direction")

        return when (direction) {
            GestureListener.Direction.LEFT -> {
                flipAnimator.rotationYBy(SWIPE_TO_LEFT_DEGREE)
                flipped = !flipped
                flipAnimator.start()
                true
            }
            GestureListener.Direction.RIGHT -> {
                flipAnimator.rotationYBy(SWIPE_TO_RIGHT_DEGREE)
                flipped = !flipped
                flipAnimator.start()
                true
            }
            GestureListener.Direction.UP -> {
                scrollAnimator.yBy(-requiredBottomPosition)
                scrollAnimation = true
                scrollAnimator.start()
                true
            }
            GestureListener.Direction.DOWN -> {
                scrollAnimator.yBy(requiredBottomPosition)
                scrollAnimation = true
                scrollAnimator.start()
                true
            }
            else -> false
        }
    }

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
            MeasureSpec.UNSPECIFIED -> {
                context.dpToPx(DEFAULT_SIZE).toInt()
            }
            MeasureSpec.AT_MOST -> {
                MeasureSpec.getSize(spec)
            }
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(spec)
            }
            else -> MeasureSpec.getSize(spec)
        }
    }

    private fun setup(newColor: Int, newStyle: Paint.Style) {
        with(paintBrush) {
            color = newColor
            style = newStyle
        }
    }
}