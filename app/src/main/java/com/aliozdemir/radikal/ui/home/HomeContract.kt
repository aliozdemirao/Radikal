package com.aliozdemir.radikal.ui.home

import com.aliozdemir.radikal.domain.model.Article

object HomeContract {

    data class UiState(
        val isLoading: Boolean = false,
        val articles: List<Article> = emptyList(),
        val error: String? = null,

        val selectedCountry: String = "us",
        val selectedCategory: String = "technology",
        val categoryMap: Map<String, String> = mapOf(
            "technology" to "Technology",
            "business" to "Business",
            "entertainment" to "Entertainment",
            "general" to "General",
            "health" to "Health",
            "science" to "Science",
            "sports" to "Sports"
        )
    )

    sealed class UiAction {
        data class OnCategorySelected(val category: String) : UiAction()
    }

    sealed class UiEffect
}