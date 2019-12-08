package com.igor.langugecards.presentation.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import com.igor.langugecards.R
import com.igor.langugecards.presentation.view.customview.extensions.dpToPx
import com.igor.langugecards.presentation.view.customview.extensions.spToPx


class LanguageCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_LOAD_TINT_COLOR = R.color.colorShimmer
        private const val DEFAULT_SIZE = 40

        private const val DEFAULT_THEME_TEXT_SIZE = 28
        private const val DEFAULT_LANGUAGE_TEXT_SIZE = 18
        private const val DEFAULT_WORD_TEXT_SIZE = 24

        private const val THEME_TEXT_MARGIN_TOP = 16
        private const val THEME_LANGUAGE_MARGIN_TOP = 48
        private const val THEME_WORD_MARGIN_TOP = 120
    }

    @ColorRes
    private var loadTintColor = DEFAULT_LOAD_TINT_COLOR
    private var cardTheme: String = context.getString(R.string.not_defined)
    private var cardLanguage: String = context.getString(R.string.not_defined)
    private var cardWord: String = context.getString(R.string.not_defined)
    private var cardThemeTextSize: Float = DEFAULT_THEME_TEXT_SIZE.toFloat()
    private var cardLanguageTextSize = DEFAULT_LANGUAGE_TEXT_SIZE.toFloat()
    private var cardWordTextSize = DEFAULT_WORD_TEXT_SIZE.toFloat()

    private val paintBrush = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()

    private var cardThemeAnimate: Boolean = false

    private var centerX: Float = 0f

    private var themeCursorY: Float = 0f

    private var languageCursorY: Float = 0f

    private var wordCursorY: Float = 0f

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
        }

        setup()
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)     //1206, 828
        Log.e("CardView", "onDraw")

        paintBrush.textSize = cardThemeTextSize
        paintBrush.textAlign = Paint.Align.CENTER

        canvas.drawText(cardTheme, centerX, themeCursorY, paintBrush)

        paintBrush.textSize = cardLanguageTextSize
        canvas.drawText(cardLanguage, centerX, languageCursorY, paintBrush)

        paintBrush.textSize = cardWordTextSize
        canvas.drawText(cardWord, centerX, wordCursorY, paintBrush)
    }

    private fun setup() {
        with(paintBrush) {
            color = Color.RED
            style = Paint.Style.FILL
        }
    }
}