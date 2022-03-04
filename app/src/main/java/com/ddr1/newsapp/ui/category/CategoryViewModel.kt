package com.ddr1.newsapp.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddr1.newsapp.BuildConfig
import com.ddr1.newsapp.models.ArticleModel
import com.ddr1.newsapp.models.CategoryModel
import kotlinx.coroutines.launch
import org.koin.dsl.module
import kotlin.math.ceil

val categoryViewModel = module {
    factory { CategoryViewModel(get()) }
}

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    val titleToolbar = "News World Wide"
    val category by lazy { MutableLiveData<String>() }
    val articleNews by lazy { MutableLiveData<ArticleModel>() }
    val loading by lazy { MutableLiveData<Boolean>() }
    val loadingMore by lazy { MutableLiveData<Boolean>() }
    val message by lazy { MutableLiveData<String?>() }

    init {
        message.value = null
    }

    var apikey = BuildConfig.API_KEY
    var sources = ""
    var page = 1
    var total = 1
    var country = "id"

    val categories = listOf(
        CategoryModel(1, "business"),
        CategoryModel(2, "entertainment"),
        CategoryModel(3, "general"),
        CategoryModel(4, "health"),
        CategoryModel(5, "science"),
        CategoryModel(6, "sports"),
        CategoryModel(7, "technology"),
    )

    fun fetchTopNewsArticle() {
        if (page > 1) loadingMore.value = true
        else loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.fetch(apikey, page, country)
                articleNews.value = response
                val totalResults: Double = response.totalResults / 20.0
                total = ceil(totalResults).toInt()
                page++
                loading.value = false
                loadingMore.value = false
            } catch (e: Exception) {
                message.value = "There is something wrong..."
            }
        }
    }

}