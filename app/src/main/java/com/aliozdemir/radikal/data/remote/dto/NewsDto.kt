package com.aliozdemir.radikal.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(

    @SerialName("status")
    val status: String?,

    @SerialName("totalResults")
    val totalResults: Int?,

    @SerialName("articles")
    val articles: List<ArticleDto?>?,
)