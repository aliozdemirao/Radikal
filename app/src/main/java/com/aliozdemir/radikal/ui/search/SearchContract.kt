package com.aliozdemir.radikal.ui.search

import com.aliozdemir.radikal.domain.model.Article

object SearchContract {

    data class UiState(
        val isLoading: Boolean = false,
        val searchResults: List<Article> = emptyList(),
        val error: String? = null,
        val searchQuery: String = "",

        val showFilterDialog: Boolean = false,
        val filterOptions: FilterOptions = FilterOptions(),
        val languageMap: Map<String, String> = mapOf(
            "ar" to "Arabic",
            "de" to "German",
            "en" to "English",
            "es" to "Spanish",
            "fr" to "French",
            "he" to "Hebrew",
            "it" to "Italian",
            "nl" to "Dutch",
            "no" to "Norwegian",
            "pt" to "Portuguese",
            "ru" to "Russian",
            "sv" to "Swedish",
            "ud" to "Urdu",
            "zh" to "Chinese"
        ),
        val sortByMap: Map<String, String> = mapOf(
            "relevancy" to "Relevancy",
            "popularity" to "Popularity",
            "publishedAt" to "Newest First"
        ),
        val searchInMap: Map<String, String> = mapOf(
            "title" to "Title",
            "description" to "Description",
            "content" to "Content"
        ),
    )

    data class FilterOptions(
        val searchIn: List<String>? = null,
        val sources: String? = null,
        val domains: String? = null,
        val excludeDomains: String? = null,
        val fromDate: String? = null,
        val toDate: String? = null,
        val language: String? = null,
        val sortBy: String? = null,
    )

    sealed class UiAction {
        data object OnSearchClicked : UiAction()
        data class OnSearchQueryChanged(val query: String) : UiAction()
        data class OnArticleClicked(val article: Article) : UiAction()

        data object OnFilterIconClicked : UiAction()
        data object OnFilterDialogDismissed : UiAction()
        data class OnFilterApplied(val newFilterOptions: FilterOptions) : UiAction()
        data object OnClearFiltersClicked : UiAction()
    }

    sealed class UiEffect {
        data class NavigateToDetail(val article: Article) : UiEffect()
    }
}