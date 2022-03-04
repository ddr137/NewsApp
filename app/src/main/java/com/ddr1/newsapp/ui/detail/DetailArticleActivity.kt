package com.ddr1.newsapp.ui.detail

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.ddr1.newsapp.databinding.ActivityDetailArticleBinding
import com.ddr1.newsapp.databinding.CustomToolbarBinding
import com.ddr1.newsapp.models.ArticleModel

class DetailArticleActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailArticleBinding.inflate(layoutInflater) }
    private lateinit var bindingToolbar: CustomToolbarBinding
    private val detail by lazy {
        intent.getSerializableExtra("intent_article") as ArticleModel.Article
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindingToolbar = binding.toolbar
        setSupportActionBar(bindingToolbar.container)
        supportActionBar!!.apply {
            title = detail.title
            setDisplayHomeAsUpEnabled(true)
        }

        detail.let {
            val web = binding.webView
            web.loadUrl(it.url)
            web.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressTop.visibility = View.GONE
                }
            }
            val settings = web.settings
            settings.javaScriptCanOpenWindowsAutomatically = false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}