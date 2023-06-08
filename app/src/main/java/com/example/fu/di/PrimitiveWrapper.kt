package com.example.fu.di

data class PrimitiveWrapper<out T>(val value: T) // See: https://youtrack.jetbrains.com/issue/KT-18918