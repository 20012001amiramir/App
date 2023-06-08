package com.example.fu.ui.home

import coil.load
import com.example.fu.R
import com.example.fu.databinding.ItemCategoryBinding
import com.example.fu.databinding.ItemFrequetDiseaseBinding
import com.example.fu.databinding.ItemNewsBinding
import com.example.fu.model.Article
import com.example.fu.model.Category
import com.example.fu.model.Disease
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

internal fun frequentDiseaseListDelegate(
    onClick: (Disease) -> Unit
) = adapterDelegateViewBinding<Disease, Any, ItemFrequetDiseaseBinding>(
        viewBinding = { layoutInflater, parent ->
            ItemFrequetDiseaseBinding.inflate(
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
                    root.setOnClickListener { onClick(item) }
                    imageView.load(R.drawable.wheat)
                }
            }
        }
    )

internal fun newsListDelegate(
    onClick: (Article) -> Unit
) = adapterDelegateViewBinding<Article, Any, ItemNewsBinding>(
        viewBinding = { layoutInflater, parent ->
            ItemNewsBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        },
        block = {
            bind {
                with(binding) {
                    tvTitle.text = item.title
                    tvDescription.text = item.content
                    imageView.clipToOutline = true
                    imageView.load(item.urlToImages.first())
                    root.setOnClickListener { onClick(item) }
                }
            }
        }
    )

internal fun categoriesListDelegate(
    onClick: (Category) -> Unit
) = adapterDelegateViewBinding<Category, Any, ItemCategoryBinding>(
        viewBinding = { layoutInflater, parent ->
            ItemCategoryBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        },
        block = {
            bind {
                with(binding) {
                    val resources = binding.root.context.resources
                    tvCountOfDisease.text = resources.getString(
                        R.string.subtitle_count_of_diseases, item.countOfDiseases.toString()
                    )
                    tvPlantName.text = item.plantName
                    imageView.clipToOutline = true
                    imageView.load(R.drawable.ic_wheat_24)//item.urlToImage)
                    root.setOnClickListener { onClick(item) }
                }
            }
        }
    )
