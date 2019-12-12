package com.igor.langugecards.presentation.view.custom

import android.animation.Animator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.core.view.GestureDetectorCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.igor.langugecards.R
import com.igor.langugecards.presentation.gestures.Gesture
import com.igor.langugecards.presentation.view.custom.extensions.dpToPx
import com.igor.langugecards.presentation.view.custom.extensions.spToPx


class LanguageCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {

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

        private const val NORMAL_ROTATION_DEGREE = 0f
        private const val FLIPPED_ROTATION_DEGREE = 180F
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

    private var mDetector: GestureDetectorCompat

    private val flipAnimator = animate()

    private val scrollAnimator = animate()

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
                    .setDuration(500)
                    .interpolator = FastOutSlowInInterpolator()

            scrollAnimator
                    .setDuration(700)
                    .interpolator = FastOutSlowInInterpolator()
        }

        mDetector = GestureDetectorCompat(context, this)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

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

    private fun setup(newColor: Int, newStyle: Paint.Style) {
        with(paintBrush) {
            color = newColor
            style = newStyle
        }
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        Log.d("CardView", "onScroll")

        val direction = Gesture.getDirection(distanceX, distanceY)

        return when (direction) {
            Gesture.Direction.LEFT -> {
                flipAnimator.rotationYBy(180f)
                        .setListener()
                flipAnimator.start()
                true
            }
            Gesture.Direction.RIGHT -> {
                flipAnimator.rotationYBy(-180f)
                flipAnimator.start()
                true
            }
            Gesture.Direction.UP -> {
                scrollAnimator.yBy(-(y + height.toFloat()))
                scrollAnimator.start()
                true
            }
            Gesture.Direction.DOWN -> {
                scrollAnimator.yBy(y + height.toFloat())
                scrollAnimator.start()
                true
            }
            else -> false
        }
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }
}