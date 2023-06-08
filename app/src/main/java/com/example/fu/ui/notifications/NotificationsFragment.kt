package com.example.fu.ui.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fu.R
import com.example.fu.databinding.FragmentNotificationsBinding
import com.example.fu.di.Scopes
import com.example.fu.di.factory.viewModels

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {


    private val binding by viewBinding(FragmentNotificationsBinding::bind)

    val viewModel: NotificationsViewModel by viewModels(Scopes.APP_SCOPE , Scopes.APP_ACTIVITY_SCOPE)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }

        binding.webView.settings.javaScriptEnabled = true;
        binding.webView.loadUrl("https://recyclemap.ru/");
    }
}