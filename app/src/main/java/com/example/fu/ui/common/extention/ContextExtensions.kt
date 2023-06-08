package com.example.fu.ui.common.extention

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import com.example.fu.util.dpToPx

fun Context.getDimension(@DimenRes dimenId: Int): Float = resources.getDimension(dimenId)

fun Context.dpToPx(@Dimension(unit = Dimension.DP) dp: Float) = dpToPx(resources.displayMetrics, dp)

fun Activity.getWindowHeight(): Int {
    // Calculate window height for fullscreen use
    val displayMetrics = DisplayMetrics()
    (this as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}