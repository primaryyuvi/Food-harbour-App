package com.yuvarajcode.food_harbor.domain.model

import com.google.gson.annotations.SerializedName


data class News(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("totalResults")
    val totalResults: Int = 0,
    @SerializedName("articles")
    val newsAttributes: List<NewsAttributes> = emptyList()
)


data class NewsAttributes(
    val source: Source? = Source("", ""),
    val author: String? = "",
    val title: String? = "",
    val description: String? = "",
    val url: String? = "",
    val urlToImage: String? = "",
    val publishedAt: String? = "",
    val content: String? = ""
)

data class Source(
    val id: Any?,
    val name: String?
)