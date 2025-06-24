package com.aliozdemir.radikal.navigation

import com.aliozdemir.radikal.domain.model.Article
import kotlinx.serialization.Serializable

sealed interface Screen {
    val routeName: String
        get() = this::class.qualifiedName ?: error("Screen must have a qualifiedName for navigation")

    @Serializable
    data object Home : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Bookmark : Screen

    @Serializable
    data class Detail(val article: Article) : Screen
}