package com.example.fu.ui.disease_detail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.R
import com.example.fu.databinding.FragmentDiseaseDetailBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import com.example.fu.ui.carouselLayoutManager
import com.example.fu.ui.common.extention.swapItems
import com.example.fu.ui.photosListDelegate
import com.example.fu.util.openUrl
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.launch

class DiseaseDetailFragment: Fragment(R.layout.fragment_disease_detail) {

    private val diseaseDetailViewModel:
            DiseaseDetailViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)

    private val binding by viewBinding(FragmentDiseaseDetailBinding::bind)

    private val args: DiseaseDetailFragmentArgs by navArgs()

    private val photosListDelegate = ListDelegationAdapter(
        photosListDelegate(onClick = { url ->  } )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diseaseDetailViewModel.fetchDisease(args.diseaseId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
    }

    private fun setupUi() {
        with(binding) {
            applyInsets()

            topAppBar.apply {
                setNavigationOnClickListener { findNavController().popBackStack() }
                setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.bookmark -> {
                            diseaseDetailViewModel.bookmarkToggle(args.diseaseId)
                            true
                        }
                        else -> false
                    }
                }
            }

            rvPhotos.apply {
                adapter = photosListDelegate
                layoutManager = carouselLayoutManager()
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    diseaseDetailViewModel.uiState.collect { state ->
                        handleUiState(state)
                    }
                }
                launch {
                    diseaseDetailViewModel.isInBookmark.collect { isInBookmark ->

                        val icon = ContextCompat.getDrawable(
                            requireContext(),
                            if (isInBookmark)
                                R.drawable.ic_bookmark_filled_24
                            else
                                R.drawable.ic_bookmark_24
                        )

                        binding.topAppBar.menu.findItem(R.id.bookmark).icon = icon
                    }
                }
            }
        }
    }

    private fun handleUiState(uiState: DiseaseDetailUiState) {
        when(uiState) {
            is DiseaseDetailUiState.Success -> {
                with(binding) {

                    topAppBar.title = uiState.disease.diseaseName
                    tvDescription.text = uiState.disease.description
                    tvSource.text = uiState.disease.sourceName
                    tvDanger.text = uiState.disease.danger
                    tvSymptoms.text = uiState.disease.symptoms.joinToString(", ")
                    tvTypeOfDisease.text = uiState.disease.type
                    tvTypeOfTreatment.text = uiState.disease.treatment

                    btnReadMore.setOnClickListener {
                        requireContext().openUrl(uiState.disease.urlToSource)
                    }

                    photosListDelegate.swapItems(uiState.disease.urlToImages)
                }
            }
            is DiseaseDetailUiState.Idle -> Unit
        }
    }

}

private fun FragmentDiseaseDetailBinding.applyInsets() {
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