package com.ddr1.newsapp.models

import java.io.Serializable

data class SourceModel(
    val sources: List<Source>,
    val status: String
) {
    data class Source(
        val category: String,
        val country: String,
        val description: String,
        val id: String,
        val language: String,
        val name: String,
        val url: String
    ): Serializable
}