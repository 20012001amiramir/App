package com.example.fu.ui.common.extention

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

fun BottomSheetDialog.forceExpandedState() {
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
    behavior.skipCollapsed = true
}

// TODO: Try to implement this using styles.
//   This solution apparently was taken from this comment https://github.com/material-components/material-components-android/pull/437#issuecomment-636330706
//   But here is an info about a new style attribute https://stackoverflow.com/a/58936096
//   Also I see an attempt to do so in our styles. See `ShapeAppearanceOverlay.SchoolBoy.BottomSheetDialog`.
//   Check it later.
fun BottomSheetDialog.forceBackgroundRoundCorners(
    dialog: () -> Dialog?
) {
    fun createMaterialShapeDrawable(bottomSheet: View): MaterialShapeDrawable {
        val shapeAppearanceModel =
            ShapeAppearanceModel
                .builder(
                    context,
                    0,
                    com.example.fu.R.style.ShapeAppearanceOverlay_BottomSheetDialog
                )
                .build()
        return MaterialShapeDrawable(shapeAppearanceModel).apply {
            initializeElevationOverlay(context)
            val currentMaterialShapeDrawable = bottomSheet.background as? MaterialShapeDrawable?
            if (currentMaterialShapeDrawable != null) {
                fillColor = currentMaterialShapeDrawable.fillColor
                tintList = currentMaterialShapeDrawable.tintList
                elevation = currentMaterialShapeDrawable.elevation
                strokeWidth = currentMaterialShapeDrawable.strokeWidth
                strokeColor = currentMaterialShapeDrawable.strokeColor
            }

        }
    }

    fun setBackgroundDrawable(bottomSheet: View) {
        val newMaterialShapeDrawable = createMaterialShapeDrawable(bottomSheet)
        ViewCompat.setBackground(bottomSheet, newMaterialShapeDrawable)
    }

    val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                setBackgroundDrawable(bottomSheet)
            }
        }
    }
    behavior.addBottomSheetCallback(bottomSheetCallback)
    setOnShowListener {
        val bottomSheet: FrameLayout = findViewById<View>(com.example.fu.R.id.design_bottom_sheet) as FrameLayout
        setBackgroundDrawable(bottomSheet)
    }
}

fun BottomSheetDialog.setupFullHeight(activity: Activity) {
    val bottomSheet: FrameLayout = findViewById<View>(com.example.fu.R.id.design_bottom_sheet) as FrameLayout
    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
    val layoutParams = bottomSheet.layoutParams
    val windowHeight = activity.getWindowHeight()
    if (layoutParams != null) {
        layoutParams.height = windowHeight
    }
    bottomSheet.layoutParams = layoutParams
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
    behavior.skipCollapsed = true
}