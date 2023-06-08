package com.example.fu.ui.common.list.stickyheaders

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

/**
 * Variant of the [ListDelegationAdapter] based on [AbsDelegationAdapterWithStickyHeaders].
 *
 * Sticky headers is provided by a [StickyHeadersAdapterDelegate] added to this.
 * You could tweak sticky header view by overriding [setupStickyHeaderView] and [teardownStickyHeaderView].
 *
 * @see StickyHeaderLinearLayoutManager
 */
open class ListDelegationAdapterWithStickyHeaders<L : List<*>>(
    vararg delegates: AdapterDelegate<L>
) : AbsDelegationAdapterWithStickyHeaders<L>(*delegates) {

    override fun getItemCount(): Int = items?.size ?: 0
}