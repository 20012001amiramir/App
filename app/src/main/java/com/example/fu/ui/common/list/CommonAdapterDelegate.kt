package com.example.fu.ui.common.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

/**
 * Make the creation of common delegate case a little easier.
 * This pass attachToParent false to just use `SomeBinding::inflate`.
 */
@Suppress("RemoveExplicitTypeArguments")
inline fun <reified I : RecyclerItem, V : androidx.viewbinding.ViewBinding> commonAdapterDelegate(
    noinline viewBindingFactory: (layoutInflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean) -> V,
    noinline initializationBlock: com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder<I, V>.() -> Unit
): AdapterDelegate<List<RecyclerItem>> {
    return adapterDelegateViewBinding<I, RecyclerItem, V>(
        { layoutInflater, parent ->
            viewBindingFactory(layoutInflater, parent, false)
        },
        block = initializationBlock
    )
}