package com.example.fu.ui.profile_edit

import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.fu.R
import com.example.fu.databinding.FragmentProfileEditBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import kotlinx.coroutines.launch

class ProfileEditFragment: Fragment(R.layout.fragment_profile_edit) {

    private val binding by viewBinding(FragmentProfileEditBinding::bind)

    private val profileEditViewModel:
            ProfileEditViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            profileEditViewModel.saveProfileImage(uri)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
    }

    private fun setupUi() {
        with(binding) {
            applyInsets()

            topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }

//            etName.addTextChangedListener { editable ->
//                profileEditViewModel.saveProfileName(editable.toString())
//            }
            btnChangePhoto.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

        }
    }

    override fun onPause() {
        super.onPause()

        profileEditViewModel.saveProfileName(binding.etName.text.toString())
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileEditViewModel.uiState.collect { state -> handleUiState(state) }
            }
        }
    }

    private fun handleUiState(uiState: ProfileEditUiState) {
        when(uiState) {
            is ProfileEditUiState.Success -> {
                with(binding) {
                    etName.setText(uiState.profile.name)
                    ivAvatar.load(uiState.profile.imagePath) {
                        placeholder(R.drawable.ic_profile_24)
                        error(R.drawable.ic_profile_24)
                    }
                }
            }
            is ProfileEditUiState.Idle -> Unit
        }
    }

}

private fun FragmentProfileEditBinding.applyInsets() {
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
