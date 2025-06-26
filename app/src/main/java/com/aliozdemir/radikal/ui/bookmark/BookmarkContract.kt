package com.aliozdemir.radikal.ui.bookmark

import com.aliozdemir.radikal.domain.model.Article

object BookmarkContract {

    data class UiState(
        val bookmarkedArticles: List<Article> = emptyList(),
    )

    sealed class UiAction {
        data object LoadBookmarkedArticles : UiAction()
        data class OnDeleteArticleClicked(val articleUrl: String) : UiAction()
        data object OnDeleteAllArticlesClicked : UiAction()
        data class OnArticleClicked(val article: Article) : UiAction()
    }

    sealed class UiEffect {
        data class NavigateToDetail(val article: Article) : UiEffect()
    }
}