package com.example.fu.ui

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.carousel.CarouselLayoutManager

internal fun Fragment.verticalLayoutManager() = LinearLayoutManager(
        context, LinearLayoutManager.VERTICAL, false
    )

internal fun Fragment.horizontalLayoutManager() = LinearLayoutManager(
        context, LinearLayoutManager.HORIZONTAL, false
    )

internal fun Fragment.gridLayoutManager(span: Int = 2) = GridLayoutManager(
        context, span
    )

internal fun Fragment.carouselLayoutManager() = CarouselLayoutManager()