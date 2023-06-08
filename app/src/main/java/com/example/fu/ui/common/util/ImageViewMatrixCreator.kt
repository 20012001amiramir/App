package com.example.fu.ui.common.util

import android.graphics.Matrix
import android.widget.ImageView

class ImageViewMatrixCreator(val imageView: ImageView) {

    fun getCenterCropMatrix(): Matrix {
        val scale = getCenterCropScale()

        return Matrix().apply {
            setScale(scale, scale)
        }
    }

    fun getCenterCropPlusShiftMatrix(percent: Float, shiftFactor: Float): Matrix {
        val scale = getCenterCropScale()
        val shift = -getAvailableHorizontalOffset() * percent

        return Matrix().apply {
            setScale(scale, scale)
            postTranslate(shift * shiftFactor, 0f)
        }
    }

    private fun getCenterCropScale(): Float {
        val drawableWidth = imageView.drawable.intrinsicWidth
        val drawableHeight = imageView.drawable.intrinsicHeight
        val imageViewWidth = imageView.measuredWidth
        val imageViewHeight = imageView.measuredHeight

        return if (drawableWidth * imageViewHeight > imageViewWidth * drawableHeight) {
            imageViewHeight / drawableHeight.toFloat()
        } else {
            imageViewWidth / drawableWidth.toFloat()
        }
    }

    private fun getAvailableHorizontalOffset(): Int {
        val drawableWidth = imageView.drawable.intrinsicWidth
        val imageViewWidth = imageView.measuredWidth

        return drawableWidth - imageViewWidth
    }
}