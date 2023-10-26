package com.example.myapplication.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        //Extract the data…
        val actionTitle = bundle?.getString("title")
        val endPoint = bundle?.getString("endPoint")

        showLoading()

        supportActionBar?.title = actionTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setting a webViewClient
        binding.webView.loadUrl(endPoint.toString())
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = WebChromeClient()
    }

    // Overriding WebViewClient functions
    inner class WebViewClient : android.webkit.WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            showLoading()
            super.onPageStarted(view, url, favicon)
        }

        // ProgressBar will disappear once page is loaded
        override fun onPageFinished(view: WebView, url: String) {
            dismissLoading()
            view.loadUrl("javascript:var footer = document.getElementById(\"footer-container\"); footer.parentNode.removeChild(footer); var header = document.getElementById(\"header-full\"); header.parentNode.removeChild(header);");
            super.onPageFinished(view, url)

        }
    }

    private fun showLoading() {
        binding.progressBar.progressLayout.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun dismissLoading() {
        binding.progressBar.progressLayout.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}