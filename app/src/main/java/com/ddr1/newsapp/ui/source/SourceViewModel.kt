package com.ddr1.newsapp.ui.source

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddr1.newsapp.BuildConfig
import com.ddr1.newsapp.models.SourceModel
import kotlinx.coroutines.launch
import org.koin.dsl.module
import kotlin.math.ceil

val sourceViewModel = module {
    factory { SourceViewModel(get()) }
}

class SourceViewModel(
    private val repository: SourceRepository
) : ViewModel() {

    val titleToolbar = "News Source"
    val sourceNews by lazy { MutableLiveData<SourceModel>() }
    val loading by lazy { MutableLiveData<Boolean>() }
    val message by lazy { MutableLiveData<String?>() }
    val loadingMore by lazy { MutableLiveData<Boolean>() }

    init {
        message.value = null
    }

    var apikey = BuildConfig.API_KEY
    var category = ""
    var total = 1

    fun fetchNewsSource() {
        if (total > 1) loadingMore.value = true
        else loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.fetch(apikey, category)
                sourceNews.value = response
                val totalResults: Double = response.sources.size / 5.0
                total = ceil(totalResults).toInt()
                total++
                loading.value = false
                loadingMore.value = false
            } catch (e: Exception) {
                message.value = "There is something wrong..."
            }
        }
    }
}