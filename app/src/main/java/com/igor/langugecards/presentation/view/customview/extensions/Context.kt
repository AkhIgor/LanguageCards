package com.igor.langugecards.presentation.view.customview.extensions

import android.content.Context


fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * resources.displayMetrics.density
}

fun Context.dpToSp(dp: Int): Float {
    return dpToPx(dp) / resources.displayMetrics.scaledDensity
}

fun Context.spToPx(sp: Int): Float {
    return sp * resources.displayMetrics.scaledDensity
}