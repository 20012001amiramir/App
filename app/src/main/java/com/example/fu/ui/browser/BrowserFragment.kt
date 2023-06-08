package com.example.fu.ui.browser

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.R
import com.example.fu.databinding.FragmentBrowserBinding

class BrowserFragment: Fragment(R.layout.fragment_browser) {

    private val binding by viewBinding(FragmentBrowserBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            applyInsets()
            webView.webViewClient = WebViewClient()
            webView.loadUrl("https://ru.wikipedia.org/wiki/%D0%91%D1%83%D1%80%D0%B0%D1%8F_%D1%80%D0%B6%D0%B0%D0%B2%D1%87%D0%B8%D0%BD%D0%B0_%D0%BF%D1%88%D0%B5%D0%BD%D0%B8%D1%86%D1%8B")
        }
    }

}

private fun FragmentBrowserBinding.applyInsets() {
    apply {
        appBarLayout.setOnApplyWindowInsetsListener { view, windowInsets ->
            WindowInsetsCompat.toWindowInsetsCompat(windowInsets).let { insets ->
                view.updatePadding(
                    top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
                )
            }
            windowInsets
        }
        root.setOnApplyWindowInsetsListener { view, windowInsets ->
            WindowInsetsCompat.toWindowInsetsCompat(windowInsets).let { insets ->
                view.updatePadding(
                    bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
                )
            }
            windowInsets
        }
    }
}