package com.example.fu.di

import android.content.Context
import toothpick.config.Module

class AppActivityModule(activitycontext: Context) :  Module() {

    init {
        bind(Context::class.java).toInstance(activitycontext)
    }
}