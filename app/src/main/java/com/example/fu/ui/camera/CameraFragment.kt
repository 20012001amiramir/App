package com.example.fu.ui.camera

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.R
import com.example.fu.databinding.FragmentCameraBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels
import com.example.fu.model.Disease
import com.example.fu.ui.common.extention.swapItems
import com.example.fu.ui.diseaseListDelegate
import com.example.fu.ui.verticalLayoutManager
import com.example.fu.util.openUrl
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date


class CameraFragment: Fragment(R.layout.fragment_camera), View.OnClickListener {

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var imageCapture: ImageCapture
    private var isBackCamera = true
    private var isLoading = false

    private val binding by viewBinding(FragmentCameraBinding::bind)

    private val cameraViewModel:
            CameraViewModel by viewModels(Scopes.APP_SCOPE, Scopes.APP_ACTIVITY_SCOPE)

    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
    )

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {

            val filename = "${Date().time.toString(16)}.jpeg"
            val fileDir = requireContext().cacheDir
            val file = File(fileDir, filename)

            val outputStream = requireContext().contentResolver.openOutputStream(file.toUri())
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            outputStream?.let { inputStream?.copyTo(it) }

            outputStream?.close()
            inputStream?.close()

            cameraViewModel.classify(file)
        }
    }

    private val searchListDelegate = ListDelegationAdapter(
        diseaseListDelegate(
            onClick = { disease -> navigateToDiseaseDetail(disease) }
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeUiState()
        closeBottomSheet()
        init()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    cameraViewModel.uiState.collect { state ->
                        handleUiState(state)
                    }
                }
                launch {
                    cameraViewModel.searchItems.collect { items ->
                        searchListDelegate.swapItems(items)
                    }
                }
            }
        }
    }

    private fun setupUi() {
        with(binding) {
            applyInsets()
            apply {
                btnClose.setOnClickListener(this@CameraFragment)
                btnFlash.setOnClickListener(this@CameraFragment)
                btnSwapCamera.setOnClickListener(this@CameraFragment)
                photoButton.setOnClickListener(this@CameraFragment)
                btnPickFromGallery.setOnClickListener(this@CameraFragment)
            }

            bottomSheet.apply {
                rvDiseaseSearch.adapter = searchListDelegate
                rvDiseaseSearch.layoutManager = verticalLayoutManager()
                etSearch.addTextChangedListener { editable ->
                    cameraViewModel.enterSearch(editable.toString())
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                    initCamera()
                } else {
                    showNoPermissions()
                }
            }
            else -> {
                showNoPermissions()
            }
        }
    }

    private fun init() {
        if (hasPermissions()) {
            initCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST_CAMERA_CODE)
        }
    }

    private fun takeAPhoto() {
        val filename = "${Date().time.toString(16)}.jpeg"
        val fileDir = requireContext().cacheDir
        val file = File(fileDir, filename)

        val outputFileOptions = ImageCapture.OutputFileOptions
            .Builder(file)
            .build()

        imageCapture.takePicture(
            outputFileOptions,
            { executor -> executor.run() },
            ImageCaptureCallback()
        )
    }

    private fun initCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(if (isBackCamera) CameraSelector.LENS_FACING_BACK else CameraSelector.LENS_FACING_FRONT)
            .build()

        imageCapture = ImageCapture.Builder()
            .setTargetResolution(Size(720, 1280))
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
    }

    private fun showNoPermissions() {
        binding.previewView.isVisible = false
        binding.noPermissionLayout.isVisible = true
    }

    private fun Uri.toBitmap(): Bitmap {
        val stream = requireContext().contentResolver.openInputStream(this)
        return BitmapFactory.decodeStream(stream)
    }

    private fun hasPermissions() = PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(
            requireContext(), permission
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnClose -> {
                findNavController().popBackStack()
            }
            R.id.btnSwapCamera -> {
                Toast.makeText(requireContext(), "Swap cam", Toast.LENGTH_SHORT).show()
            }
            R.id.btnFlash -> {
                Toast.makeText(requireContext(), "Flash", Toast.LENGTH_SHORT).show()
            }
            R.id.photoButton -> {
                if (!isLoading)
                    takeAPhoto()
            }
            R.id.btnPickFromGallery -> {
                if (!isLoading)
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
            }
        }
    }

    private fun openBottomSheet() {
        val bottomSheet = BottomSheetBehavior.from(binding.bottomSheet.root)
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun closeBottomSheet() {
        val bottomSheet = BottomSheetBehavior.from(binding.bottomSheet.root)
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun handleUiState(uiState: CameraUiState) {
        binding.bottomSheet.apply {
            when(uiState) {
                is CameraUiState.Success -> {
                    tvTitle.text = uiState.disease.diseaseName
                    tvDescription.text = uiState.disease.description
                    tvSource.text = uiState.disease.sourceName
                    tvDanger.text = uiState.disease.danger
                    tvSymptoms.text = uiState.disease.symptoms.joinToString(", ")
                    tvTypeOfDisease.text = uiState.disease.type
                    tvTypeOfTreatment.text = uiState.disease.treatment

                    binding.bottomSheet.apply {
                        btnReadMore.setOnClickListener {
                            requireContext().openUrl(uiState.disease.urlToSource)
                        }
                        btnSaveDisease.setOnClickListener {
                            cameraViewModel.saveBookmark(uiState.disease)
                            invertBookmarkButton(uiState.disease)
                        }
                        btnRemoveDisease.setOnClickListener {
                            cameraViewModel.removeBookmark(uiState.disease)
                            invertBookmarkButton(uiState.disease)
                        }

                        invertBookmarkButton(uiState.disease)
                    }

                    binding.bottomSheet.containerDescription.visibility = View.VISIBLE
                    binding.bottomSheet.containerSearch.visibility = View.GONE

                    tvResult.text = resources.getString(R.string.subtitle_disease_identified)
                    tvResult.setTextColor(ResourcesCompat.getColor(resources, R.color.light_primary_color, null))

                    tvResult.setTextColor(ResourcesCompat.getColor(resources, R.color.light_primary_color, null))

                    val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_done_24, null)?.mutate()
                    icon?.colorFilter = PorterDuffColorFilter(ResourcesCompat.getColor(resources, R.color.light_primary_color, null), PorterDuff.Mode.SRC_IN)

                    ivResultIcon.apply {
                        setImageDrawable(icon)
                        clipToOutline = true
                        background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.rounded_corners_12,
                            null
                        )
                        backgroundTintList =
                            resources.getColorStateList(R.color.light_primary_container_color, null)
                    }

                    isLoading = false
                    setProgressBarVisibility(false)
                    openBottomSheet()
                }
                is CameraUiState.Healthy -> {

                    binding.bottomSheet.containerDescription.visibility = View.GONE
                    binding.bottomSheet.containerSearch.visibility = View.VISIBLE

                    tvTitle.text = resources.getString(R.string.title_plant_is_healthy)

                    tvResult.text = resources.getString(R.string.subtitle_disease_not_found_positive)
                    tvResult.setTextColor(ResourcesCompat.getColor(resources, R.color.light_primary_color, null))

                    tvResult.setTextColor(ResourcesCompat.getColor(resources, R.color.light_primary_color, null))

                    val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_done_24, null)?.mutate()
                    icon?.colorFilter = PorterDuffColorFilter(ResourcesCompat.getColor(resources, R.color.light_primary_color, null), PorterDuff.Mode.SRC_IN)

                    ivResultIcon.apply {
                        setImageDrawable(icon)
                        clipToOutline = true
                        background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.rounded_corners_12,
                            null
                        )
                        backgroundTintList =
                            resources.getColorStateList(R.color.light_primary_container_color, null)
                    }

                    isLoading = false
                    setProgressBarVisibility(false)
                    openBottomSheet()
                }
                is CameraUiState.NothingWasFound -> {

                    binding.bottomSheet.containerDescription.visibility = View.GONE
                    binding.bottomSheet.containerSearch.visibility = View.VISIBLE

                    tvTitle.text = resources.getString(R.string.title_try_to_find_manually)

                    tvResult.text = resources.getString(R.string.subtitle_disease_not_found_negative)
                    tvResult.setTextColor(ResourcesCompat.getColor(resources, R.color.light_error_color, null))

                    val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_close_24, null)?.mutate()
                    icon?.colorFilter = PorterDuffColorFilter(ResourcesCompat.getColor(resources, R.color.light_error_color, null), PorterDuff.Mode.SRC_IN)

                    ivResultIcon.apply {
                        setImageDrawable(icon)
                        clipToOutline = true
                        background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.rounded_corners_12,
                            null
                        )
                        backgroundTintList =
                            resources.getColorStateList(R.color.light_error_container_color, null)
                    }

                    isLoading = false
                    setProgressBarVisibility(false)
                    openBottomSheet()
                }
                is CameraUiState.Error -> {
                    binding.bottomSheet.containerDescription.visibility = View.GONE
                    binding.bottomSheet.containerSearch.visibility = View.VISIBLE

                    tvTitle.text = uiState.error

                    tvResult.text = resources.getString(R.string.subtitle_disease_not_found_negative)
                    tvResult.setTextColor(ResourcesCompat.getColor(resources, R.color.light_error_color, null))

                    val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_close_24, null)?.mutate()
                    icon?.colorFilter = PorterDuffColorFilter(ResourcesCompat.getColor(resources, R.color.light_error_color, null), PorterDuff.Mode.SRC_IN)

                    ivResultIcon.apply {
                        setImageDrawable(icon)
                        clipToOutline = true
                        background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.rounded_corners_12,
                            null
                        )
                        backgroundTintList =
                            resources.getColorStateList(R.color.light_error_container_color, null)
                    }

                    isLoading = false
                    setProgressBarVisibility(false)
                    openBottomSheet()
                }
                is CameraUiState.Loading -> {
                    isLoading = true
                    setProgressBarVisibility(true)
                }
                is CameraUiState.Idle -> Unit
            }
        }
    }

    private fun invertBookmarkButton(disease: Disease) {
        with(binding.bottomSheet) {
            if (cameraViewModel.isInBookmarks(disease)) {
                btnSaveDisease.visibility = View.GONE
                btnRemoveDisease.visibility = View.VISIBLE
            } else {
                btnSaveDisease.visibility = View.VISIBLE
                btnRemoveDisease.visibility = View.GONE
            }
        }
    }

    private fun setProgressBarVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        binding.progressBar.visibility = visibility
    }

    inner class ImageCaptureCallback: ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            outputFileResults.savedUri.let { uri ->
                if (uri != null) {
                    cameraViewModel.classify(uri.toFile())
                } else {
                    CoroutineScope(Dispatchers.IO).launch{
                        Toast.makeText(requireContext(), "Ошибка при сохранении изображения", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        override fun onError(exception: ImageCaptureException) {
            CoroutineScope(Dispatchers.IO).launch{
                Toast.makeText(requireContext(), "Ошибка при сохранении изображения", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

private fun FragmentCameraBinding.applyInsets() {
    apply {
        insetsWrapper.setOnApplyWindowInsetsListener { view, windowInsets ->
            WindowInsetsCompat.toWindowInsetsCompat(windowInsets).let { insets ->
                view.updatePadding(
                    top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()).bottom
                )
            }
            windowInsets
        }
    }
}

private fun CameraFragment.navigateToDiseaseDetail(disease: Disease) {
    val directions = CameraFragmentDirections
        .actionCameraFragmentToDiseaseDetailFragment(diseaseId = disease.id)
    findNavController().navigate(directions)
}