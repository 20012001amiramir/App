package com.example.fu

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.fu.databinding.ActivityMainBinding
import com.example.fu.di.AppActivityModule
import com.example.fu.di.Scopes.APP_ACTIVITY_SCOPE
import com.example.fu.di.Scopes.APP_SCOPE
import com.example.fu.di.factory.viewModels
import com.example.fu.util.ifNotNull
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class MainActivity : AppCompatActivity() {

    private var blockingDialog: AlertDialog? = null

    private lateinit var binding: ActivityMainBinding

    private val viewModel: AppActivityViewModel by viewModels(APP_SCOPE, APP_ACTIVITY_SCOPE)

    private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private val topLevelDestinations = listOf(
        R.id.homeFragment,
        R.id.searchFragment,
        R.id.articlesFragment,
        R.id.profileFragment,
        R.id.bookmarksFragment
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        this.let {
            ActivityCompat.requestPermissions(
                it, PERMISSIONS,
                777
            )
        }

        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE))

        Toothpick.openScopes(APP_SCOPE, APP_ACTIVITY_SCOPE)
            .installModules(AppActivityModule(activitycontext = this))
            .closeOnDestroy(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.applyInsets()

        setupNavigation()
//
//        viewModel.appViewState.observe(this, ::renderAppViewState)

    }

    private fun setupNavigation() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.hostMain) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            changeVisibilityOfBottomNavigation(destination.id)
        }
    }

//    private fun changeNavigationBarColorsIfPossible(@IdRes destinationId: Int) {
//        if (versionAllowsToFullyColorizeNavBar()) {
//            window?.navigationBarColor = when (destinationId) {
//                in topLevelDestinations -> getColor(R.color.white)
//                else -> getColor(R.color.white)
//            }
//
//        }
//    }

    private fun changeVisibilityOfBottomNavigation(@IdRes destinationId: Int) {
        val visibility = if (destinationId in topLevelDestinations) View.VISIBLE else View.GONE
        binding.bottomNavigation.visibility = visibility
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        blockingDialog?.let {
            outState.putBundle(BLOCKING_DIALOG_TAG, it.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        ifNotNull(blockingDialog, savedInstanceState.getBundle(BLOCKING_DIALOG_TAG)) { dialog, bundle ->
            dialog.onRestoreInstanceState(bundle)
        }
    }

//    private fun setMainScreen(@IdRes destinationId: Int) {
//        val navController = findNavController(R.id.navHostFragment)
//        val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
//        navGraph.setStartDestination(destinationId)
//        navController.graph = navGraph
//
//        changeNavigationBarColorsIfPossible(destinationId)
//    }

//    private fun renderAppViewState(appViewState: AppActivityViewModel.AppViewState) {
//        when (appViewState) {
//            is AppActivityViewModel.AppViewState.Register -> {
//                setMainScreen(R.id.loginFragment )
//            }
//            is AppActivityViewModel.AppViewState.Login -> {
//                setMainScreen(R.id.mainEnterFragment)
//            }
//            is AppActivityViewModel.AppViewState.Schedule -> {
//                setMainScreen(R.id.navigation_notifications)
//            }
//            is AppActivityViewModel.AppViewState.QrReader -> {
//                setMainScreen(R.id.navigation_home)
//            }
//            is AppActivityViewModel.AppViewState.CurrentFragmentState -> {
//                //This is state activity should operate after auth is done to support configuration changes
//                //without replacing current fragment. In this state Android lifecycle decides the fate of current screen
//            }
//            else -> {}
//        }
//    }
    companion object {
        const val LOGOUT_KEY = "LOGOUT_KEY"
        const val ID_CURRENT_PAGE = "ID_CURRENT_PAGE"
        private const val LOADING_DIALOG_TAG = "LOADING_DIALOG_TAG"
        private const val BLOCKING_DIALOG_TAG = "BLOCKING_DIALOG_TAG"
    }
}

private fun ActivityMainBinding.applyInsets() {
    apply {
        bottomNavigation.setOnApplyWindowInsetsListener { view, windowInsets ->
            WindowInsetsCompat.toWindowInsetsCompat(windowInsets).let { insets ->
                view.updatePadding(
                    bottom = insets.getInsets(
                        WindowInsetsCompat.Type.systemBars() or
                                WindowInsetsCompat.Type.ime()
                    ).bottom
                )
            }
            windowInsets
        }
    }
}