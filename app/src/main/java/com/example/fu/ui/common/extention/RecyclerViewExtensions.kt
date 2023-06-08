package com.example.fu.ui.common.extention

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fu.ui.common.list.decoration.DividerItemDecoration

fun RecyclerView.addDivider(orientation: Int, @DrawableRes drawableResId: Int) {
    addItemDecoration(
        DividerItemDecoration(context, orientation).apply {
            setDrawable(resources.getDrawable(drawableResId, null))
        }
    )
}

fun RecyclerView.makeItemVisibleOnScreen(index: Int, smoothScroller: RecyclerView.SmoothScroller? = null) {
    // Scroll to this position.
    when (val layoutManager = this.layoutManager) {
        is LinearLayoutManager -> {
            if (index !in layoutManager.findFirstCompletelyVisibleItemPosition()..layoutManager.findLastCompletelyVisibleItemPosition()) {
                if (smoothScroller == null) {
                    smoothScrollToPosition(index)
                } else {
                    smoothScroller.targetPosition = index
                    layoutManager.startSmoothScroll(smoothScroller)
                }
            }
        }
        else -> {
            // Currently we do not support other layout managers.
        }
    }
}