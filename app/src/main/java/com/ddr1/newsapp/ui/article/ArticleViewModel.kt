package com.ddr1.newsapp.ui.article

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddr1.newsapp.BuildConfig
import com.ddr1.newsapp.models.ArticleModel
import kotlinx.coroutines.launch
import org.koin.dsl.module
import kotlin.math.ceil

val articleViewModel = module {
    factory { ArticleViewModel(get()) }
}

class ArticleViewModel(
    private val repository: ArticleRepository
) : ViewModel() {

    val titleToolbar = "News Article"
    val articleNews by lazy { MutableLiveData<ArticleModel>() }
    val loading by lazy { MutableLiveData<Boolean>() }
    val loadingMore by lazy { MutableLiveData<Boolean>() }
    val message by lazy { MutableLiveData<String?>() }

    init {
        message.value = null
    }

    var apikey = BuildConfig.API_KEY
    var sources = ""
    var query = ""
    var page = 1
    var total = 1

    fun fetchNewsArticle() {
        if (page > 1) loadingMore.value = true
        else loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.fetch(apikey, sources, query, page)
                articleNews.value = response
                val totalResults: Double = response.totalResults / 20.0
                total = ceil(totalResults).toInt()
                page ++
                loading.value = false
                loadingMore.value = false
            } catch (e: Exception) {
                message.value = "There is something wrong..."
            }
        }
    }
}