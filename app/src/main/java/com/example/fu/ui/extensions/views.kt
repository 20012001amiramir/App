package com.example.fu.ui.extensions

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.example.fu.R
import com.google.android.material.snackbar.Snackbar


fun showErrorSnackbar(context: Context, stringRes: Int, view: View) {
    val snackbar = Snackbar.make(view, stringRes, Snackbar.LENGTH_LONG)
    snackbar.setBackgroundTint(Color.RED)
    snackbar.setTextColor(Color.WHITE)
    snackbar.show()
}

fun showErrorSnackbar(context: Context, str: String, view: View) {
    val snackbar = Snackbar.make(view, str, Snackbar.LENGTH_LONG)
    snackbar.setBackgroundTint(Color.RED)
    snackbar.setTextColor(Color.WHITE)
    snackbar.show()
}

fun View.updateMargin(left: Int? = null, right: Int? = null, top: Int? = null, bottom: Int? = null) {
    val params = layoutParams
    if (params is MarginLayoutParams) {
        params.setMargins(left ?: marginLeft, top ?: marginTop,
            right ?: marginRight, bottom ?: marginBottom)
        layoutParams = params
        requestLayout()
    }
}