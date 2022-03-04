package com.ddr1.newsapp.ui.source

import com.ddr1.newsapp.network.ApiClient
import com.ddr1.newsapp.models.SourceModel
import org.koin.dsl.module

val sourceRepository = module {
    factory { SourceRepository(get())  }
}
class SourceRepository(
    private val api: ApiClient
) {

    suspend fun fetch(
        apikey: String,
        category: String
    ): SourceModel {
        return api.fetchNewsSource(apikey, category)
    }

}