package com.example.fu.ui.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.load
import com.example.fu.R
import com.example.fu.databinding.FragmentProfileBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val profileViewModel:
            ProfileViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.uiState.collect { state -> handleUiState(state) }
            }
        }
    }

    private fun setupUi() {
        with(binding) {
            applyInsets()

            topAppBar.apply {
                setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.edit -> {
                            navigateToProfileEdit()
                            true
                        }
                        else -> false
                    }
                }
            }
        }
    }

    private fun handleUiState(uiState: ProfileUiState) {
        when(uiState) {
            is ProfileUiState.Idle -> Unit
            is ProfileUiState.Success -> {
                with(binding) {
                    tvName.text = uiState.profile.name.ifEmpty { "No Name" }
                    ivAvatar.load(data = uiState.profile.imagePath) {
                        placeholder(R.drawable.ic_profile_24)
                        error(R.drawable.ic_profile_24)
                    }
                }
            }
        }
    }

}

private fun FragmentProfileBinding.applyInsets() {
    apply {
        appBarLayout.setOnApplyWindowInsetsListener { view, windowInsets ->
            WindowInsetsCompat.toWindowInsetsCompat(windowInsets).let { insets ->
                view.updatePadding(
                    top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                )
            }
            windowInsets
        }
    }
}

private fun ProfileFragment.navigateToProfileEdit() {
    findNavController().navigate(R.id.action_navigationProfile_to_profileEditFragment)
}