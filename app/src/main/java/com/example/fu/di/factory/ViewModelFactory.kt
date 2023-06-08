package com.example.fu.di.factory

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.smoothie.lifecycle.closeOnDestroy
import toothpick.smoothie.viewmodel.closeOnViewModelCleared

inline fun <reified VM : ViewModel> Fragment.viewModels(
    parentScope: String,
    scope: String,
    crossinline onScopeConfigure: (scope: Scope) -> Unit = {}
) = viewModels<VM> {
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val currentScope = Toothpick.openScopes(parentScope, scope)
            if (!Toothpick.isScopeOpen(currentScope)) {
                onScopeConfigure(currentScope)
                currentScope.closeOnViewModelCleared(this@viewModels)
            }

            return currentScope.getInstance(modelClass)
        }
    }
}

inline fun <reified VM : ViewModel> ComponentActivity.viewModels(
    parentScope: String,
    scope: String,
    crossinline onScopeConfigure: (scope: Scope) -> Unit = {}
) = viewModels<VM> {
    object : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val currentScope = Toothpick.openScopes(parentScope, scope)
            if (!Toothpick.isScopeOpen(currentScope)) {
                onScopeConfigure(currentScope)
                currentScope.closeOnDestroy(this@viewModels)
            }

            return currentScope.getInstance(modelClass)
        }
    }
}

// TODO: (Discuss) I think this variant is better because we use lambda from other variant only for assisted injection, f
//  and with these modifications we have shorter API on the call side.
inline fun <reified VM : ViewModel> Fragment.viewModelsAssisted(
    parentScope: String,
    newScope: String,
    crossinline configureNewScope: Module.() -> Unit = {}
) = viewModels<VM> {
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val scope = Toothpick.openScopes(parentScope, newScope)
            if (!Toothpick.isScopeOpen(scope)) {
                scope.installModules(
                    object : Module() {
                        init {
                            this.configureNewScope()
                        }
                    }
                )
                scope.closeOnViewModelCleared(this@viewModelsAssisted)
            }
            return scope.getInstance(modelClass)
        }
    }
}
