package com.example.fu.ui.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.withStyledAttributes
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.fu.R
import com.example.fu.ui.common.extention.getEnum

class LoadableImageView(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    var onLoadSuccessListener: (() -> Unit)? = null
    var onLoadFailureListener: ((Throwable?) -> Unit)? = null
    var placeholder: Drawable? = null
    var error: Drawable? = null

    enum class LoadScaling { ScaleToFit, DownscaleToFit, ScaleToFitAndCrop }

    var loadScaling: LoadScaling = LoadScaling.ScaleToFit

    var isLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
        private set

    var loadListener = object : RequestListener<Drawable?> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable?>?,
            isFirstResource: Boolean
        ): Boolean {
            isLoaded.value = false
            onLoadFailureListener?.invoke(e)
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable?>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            isLoaded.value = true
            onLoadSuccessListener?.invoke()
            return false
        }
    }

    init {
//        context.withStyledAttributes(attrs, R.styleable.LoadableImageView) {
//            placeholder = getDrawable(R.styleable.LoadableImageView_placeholder)
//            error = getDrawable(R.styleable.LoadableImageView_error)
//            loadScaling = getEnum(R.styleable.LoadableImageView_loadScaling, loadScaling)
//        }
    }



    fun load(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(placeholder)
            .error(error)
            .run {
                when(loadScaling) {
                    LoadScaling.ScaleToFit -> fitCenter()
                    LoadScaling.DownscaleToFit -> centerInside()
                    LoadScaling.ScaleToFitAndCrop -> centerCrop()
                }
            }
            .listener(loadListener)
            .into(this)
    }
}