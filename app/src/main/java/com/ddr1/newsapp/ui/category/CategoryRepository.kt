package com.ddr1.newsapp.ui.category

import com.ddr1.newsapp.models.ArticleModel
import com.ddr1.newsapp.network.ApiClient
import org.koin.dsl.module

val categoryRepository = module {
    factory { CategoryRepository(get())  }
}
class CategoryRepository(
    private val api: ApiClient
) {

    suspend fun fetch(
        apikey: String,
        page: Int,
        country: String,
    ): ArticleModel {
        return api.fetchTopNewsArticle(apikey, page, country)
    }

}