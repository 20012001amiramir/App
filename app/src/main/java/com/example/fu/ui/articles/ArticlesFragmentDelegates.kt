package com.example.fu.ui.articles

import coil.load
import com.example.fu.databinding.ItemArticleBinding
import com.example.fu.model.Article
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

internal fun articlesListDelegate(
    onClick: (Article) -> Unit
) = adapterDelegateViewBinding<Article, Any, ItemArticleBinding>(
        viewBinding = { layoutInflater, parent ->
            ItemArticleBinding.inflate(
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