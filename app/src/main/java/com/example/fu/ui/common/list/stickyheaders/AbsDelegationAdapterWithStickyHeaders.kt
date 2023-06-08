package com.example.fu.ui.common.list.stickyheaders

import com.hannesdorfmann.adapterdelegates4.AbsDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager


/**
 * Variant of the [AbsDelegationAdapter] with support of the [StickyHeaderLinearLayoutManager].
 *
 * Sticky headers is provided by a [StickyHeadersAdapterDelegate] added to this.
 * You could tweak sticky header view by overriding [setupStickyHeaderView] and [teardownStickyHeaderView].
 */
abstract class AbsDelegationAdapterWithStickyHeaders<T>(
    vararg delegates: AdapterDelegate<T>
) : AbsDelegationAdapter<T>(AdapterDelegatesManagerWithStickyHeaders(*delegates)),
    StickyHeaderLinearLayoutManager.StickyHeaderCallbacks {

    final override fun isStickyHeader(position: Int): Boolean {
        val viewType = getItemViewType(position)
        val manager = delegatesManager as AdapterDelegatesManagerWithStickyHeaders
        return manager.isViewTypeForStickyHeaders(viewType)
    }
}

/**
 * Unwraps [StickyHeadersAdapterDelegate] and tracks which delegates are for sticky headers.
 */
private class AdapterDelegatesManagerWithStickyHeaders<T>(
    vararg delegates: AdapterDelegate<T>
) : AdapterDelegatesManager<T>() {

    private val viewTypesOfStickyHeaders = mutableListOf<Int>()

    init {
        delegates.forEach { addDelegate(it) }
    }

    override fun addDelegate(
        viewType: Int,
        allowReplacingDelegate: Boolean,
        delegate: AdapterDelegate<T>
    ): AdapterDelegatesManager<T> {

        var realDelegate = delegate
        if (delegate is StickyHeadersAdapterDelegate) {
            viewTypesOfStickyHeaders += viewType
            realDelegate = delegate.realDelegate
        }
        return super.addDelegate(viewType, allowReplacingDelegate, realDelegate)
    }

    override fun removeDelegate(delegate: AdapterDelegate<T>): AdapterDelegatesManager<T> {
        var realDelegate = delegate
        if (delegate is StickyHeadersAdapterDelegate) {
            realDelegate = delegate.realDelegate
            viewTypesOfStickyHeaders -= getViewType(realDelegate)
        }
        return super.removeDelegate(realDelegate)
    }

    fun isViewTypeForStickyHeaders(viewType: Int) = viewType in viewTypesOfStickyHeaders
}