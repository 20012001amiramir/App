package com.example.fu.ui.common.list.stickyheaders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

/**
 * Special wrapper of the [AdapterDelegate] to be **only** used in [AbsDelegationAdapterWithStickyHeaders]
 * and it's subclasses to provide sticky headers.
 *
 * **Note: Overridden abstract methods will always throw** (This class is just marking wrapper,
 * but because of protected methods in AdapterDelegate, regular Decorator pattern is not possible)
 *
 * @see AbsDelegationAdapterWithStickyHeaders
 */
class StickyHeadersAdapterDelegate<T>(val realDelegate: AdapterDelegate<T>): AdapterDelegate<T>() {

    //region Unsupported operations (this class is just marking wrapper)

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        throw UnsupportedOperationException("Must not be used as regular AdapterDelegate")
    }

    override fun isForViewType(items: T, position: Int): Boolean {
        throw UnsupportedOperationException("Must not be used as regular AdapterDelegate")
    }

    override fun onBindViewHolder(
        items: T,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        throw UnsupportedOperationException("Must not be used as regular AdapterDelegate")
    }

    //endregion
}

fun <T> AdapterDelegate<T>.forStickyHeaders(): AdapterDelegate<T> {
    return StickyHeadersAdapterDelegate(this)
}