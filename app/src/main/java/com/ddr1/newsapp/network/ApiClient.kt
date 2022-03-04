package com.ddr1.newsapp.network

import com.ddr1.newsapp.models.ArticleModel
import com.ddr1.newsapp.models.SourceModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("top-headlines/sources")
    suspend fun fetchNewsSource(
        @Query("apikey") apikey: String,
        @Query("category") category: String
    ): SourceModel

    @GET("everything")
    suspend fun fetchNewsArticle(
        @Query("apikey") apikey: String,
        @Query("sources") category: String,
        @Query("q") query: String,
        @Query("page") page: Int,
    ): ArticleModel

    @GET("top-headlines")
    suspend fun fetchTopNewsArticle(
        @Query("apikey") apikey: String,
        @Query("page") page: Int,
        @Query("country") country: String,
    ): ArticleModel

}