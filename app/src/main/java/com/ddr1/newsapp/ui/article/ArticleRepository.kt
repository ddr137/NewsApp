package com.ddr1.newsapp.ui.article

import com.ddr1.newsapp.network.ApiClient
import com.ddr1.newsapp.models.ArticleModel
import org.koin.dsl.module

val articleRepository = module {
    factory { ArticleRepository(get())  }
}
class ArticleRepository(
    private val api: ApiClient
) {

    suspend fun fetch(
        apikey: String,
        sources: String,
        query: String,
        page: Int
    ): ArticleModel {
        return api.fetchNewsArticle(apikey, sources, query, page)
    }

}