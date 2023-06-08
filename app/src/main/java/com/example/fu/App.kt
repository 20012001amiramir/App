package com.example.fu

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.fu.di.AppModule
import com.example.fu.di.Scopes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import toothpick.Toothpick

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initToothpick()
    }

    private fun initToothpick() {
        Toothpick
            .openScope(Scopes.APP_SCOPE)
            .installModules(AppModule(context = this))
    }

}
