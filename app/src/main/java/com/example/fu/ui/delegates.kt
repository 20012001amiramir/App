package com.example.fu.ui

import coil.load
import com.example.fu.data.network.response.AddGarbageResponse
import com.example.fu.data.network.response.DataTokenForGarbage
import com.example.fu.data.network.response.GarbageType
import com.example.fu.databinding.ItemCarouselBinding
import com.example.fu.databinding.ItemDiseaseBinding
import com.example.fu.databinding.ItemGarbageBinding
import com.example.fu.databinding.ItemGarbageTypeBinding
import com.example.fu.model.Disease
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

internal fun photosListDelegate(
    onClick: (String) -> Unit
) = adapterDelegateViewBinding<String, Any, ItemCarouselBinding>(
        viewBinding = { layoutInflater, parent ->
            ItemCarouselBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        },
        block = {
            bind {
                with(binding) {
                    carouselImageView.load(item)
                    root.setOnClickListener { onClick(item) }
                }
            }
        }
    )

internal fun diseaseListDelegate(
    onClick: (Disease) -> Unit
    ) = adapterDelegateViewBinding<Disease, Any, ItemDiseaseBinding>(
        viewBinding = { layoutInflater, parent ->
            ItemDiseaseBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        },
        block = {
            bind {
                with(binding) {
                    tvPlantName.text = item.plantName
                    tvDiseaseName.text = item.diseaseName
                    tvDescription.text = item.description
                    root.setOnClickListener { onClick(item) }
                    imageView.clipToOutline = true
                    imageView.load(item.urlToImages.first())
                }
            }
        }
    )