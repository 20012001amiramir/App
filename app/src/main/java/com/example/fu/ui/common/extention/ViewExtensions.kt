package com.example.fu.ui.common.extention

import android.app.Activity
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.example.fu.R
import com.example.fu.util.dpToPx

fun <F : Fragment> View.findFragmentOrNull(): F? =
    try {
        FragmentManager.findFragment(this)
    } catch (error: IllegalStateException) {
        null
    }

fun ViewGroup.getOrAddChildView(index: Int, newInstance: (ViewGroup) -> View): View =
    getChildAt(index)
        ?: newInstance(this).also { addView(it) }

fun ViewGroup.makeChildViewsGone(range: IntRange) {
    val available = 0 until childCount
    var i = range.first
    while (i in range && i in available) {
        getChildAt(i)?.isVisible = false
        i++
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.dpToPx(@Dimension(unit = Dimension.DP) dp: Float): Float =
    dpToPx(resources.displayMetrics, dp)

fun View.setFitsSystemWindowsForParents(fitSystemWindows: Boolean) {
    (parent as? View)?.let { parentView ->
        parentView.fitsSystemWindows = fitSystemWindows
        parentView.setFitsSystemWindowsForParents(fitSystemWindows)
    }
}

fun View.setSystemUiFlag(flag: Int, enabled: Boolean) {
    systemUiVisibility = (systemUiVisibility and flag.inv()) or (if (enabled) flag else 0)
}

inline fun <reified T : Enum<T>> TypedArray.getEnum(attr: Int, default: T): T {
    return getInteger(attr, -1).let {
        if (it >= 0) enumValues<T>()[it] else default
    }
}

fun Activity.showSnackError(message: CharSequence?) {
    val nonNullMessage = if (message != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(message.toString())
        }
    } else {
        "Произошла неизвестная ошибка"
    }
    val viewGroup = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
    with(Snackbar.make(viewGroup, nonNullMessage, Snackbar.LENGTH_LONG)) {

        // хак, прочитанный на http://stackoverflow.com/a/31797046/995020
        with(view.findViewById<TextView>(R.id.snackbar_text)) {
            setTextColor(Color.RED)
            maxLines = 5
            linksClickable = true
            movementMethod = LinkMovementMethod.getInstance()
            setLinkTextColor(Color.CYAN)
        }
        view.setBackgroundColor(Color.RED)
        show()
    }
}

fun Activity.showSnack(message: CharSequence?) {
    val nonNullMessage = message ?: "Неизвестная ошибка"
    val viewGroup = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
    Snackbar.make(viewGroup, nonNullMessage, Snackbar.LENGTH_SHORT).show()
}