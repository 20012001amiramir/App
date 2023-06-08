package com.example.fu.ui.common.extention

import com.hannesdorfmann.adapterdelegates4.AbsDelegationAdapter

// TODO: Is it possible to solve this problem with DiffUtil?
inline fun <reified T, reified R : Any> AbsDelegationAdapter<MutableList<T>>.firstOrNull(): R? = items?.firstOrNull()?.let { if (it is R) it else null }

fun <T> AbsDelegationAdapter<List<T>>.add(item: T) = (items as MutableList<T>).add(item)

fun <T> AbsDelegationAdapter<List<T>>.add(position: Int, item: T) = (items as MutableList<T>).add(position, item)

fun <T> AbsDelegationAdapter<List<T>>.addAll(collection: Collection<T>) = (items as MutableList<T>).addAll(collection)

fun <T> AbsDelegationAdapter<List<T>>.clear() = (items as MutableList<T>).clear()

fun <T> AbsDelegationAdapter<List<T>>.removeAt(position: Int) = (items as MutableList<T>).removeAt(position)

fun <T> AbsDelegationAdapter<T>.swapItems(newItems: T) {
    items = newItems
    notifyDataSetChanged()
}
