package com.example.fu.util

import android.util.LruCache

inline fun <K, V> LruCache<K, V>.getOrPut(key: K, defaultValue: () -> V): V =
    synchronized(this) {
        val value = get(key)
        return if (value == null) {
            val newValue = defaultValue()
            put(key, newValue)
            newValue
        } else {
            value
        }
    }