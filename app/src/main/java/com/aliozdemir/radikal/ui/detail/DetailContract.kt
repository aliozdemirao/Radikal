package com.aliozdemir.radikal.ui.detail

import com.aliozdemir.radikal.domain.model.Article

object DetailContract {

    data class UiState(
        val article: Article? = null
    )

    sealed class UiAction {
        data class LoadArticleDetails(val article: Article) : UiAction()
        data object OnBrowserClicked : UiAction()
        data object OnShareClicked : UiAction()
        data object OnNavigateUpClicked : UiAction()
    }

    sealed class UiEffect {
        data class OpenUrlInBrowser(val url: String) : UiEffect()
        data class ShareArticle(val url: String) : UiEffect()
        data object NavigateBack : UiEffect()
    }
}