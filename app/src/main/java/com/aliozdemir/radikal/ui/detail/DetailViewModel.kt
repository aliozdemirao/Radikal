package com.aliozdemir.radikal.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.domain.usecase.DeleteArticleByUrlUseCase
import com.aliozdemir.radikal.domain.usecase.InsertArticleUseCase
import com.aliozdemir.radikal.domain.usecase.IsArticleBookmarkedUseCase
import com.aliozdemir.radikal.ui.detail.DetailContract.UiAction
import com.aliozdemir.radikal.ui.detail.DetailContract.UiEffect
import com.aliozdemir.radikal.ui.detail.DetailContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val insertArticleUseCase: InsertArticleUseCase,
    private val deleteArticleByUrlUseCase: DeleteArticleByUrlUseCase,
    private val isArticleBookmarkedUseCase: IsArticleBookmarkedUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    fun handleUiAction(action: UiAction) {
        when (action) {
            is UiAction.LoadArticleDetails -> loadArticle(action.article)
            UiAction.OnBookmarkClicked -> toggleBookmarkStatus()
            UiAction.OnBrowserClicked -> openArticleInBrowser()
            UiAction.OnShareClicked -> shareArticle()
            UiAction.OnNavigateUpClicked -> navigateBack()
        }
    }

    private fun loadArticle(article: Article) {
        updateUiState { copy(article = article) }
        viewModelScope.launch {
            val isBookmarked = isArticleBookmarkedUseCase(article.url!!)
            updateUiState { copy(isBookmarked = isBookmarked) }
        }
    }

    private fun toggleBookmarkStatus() {
        viewModelScope.launch {
            val currentlyBookmarked = _uiState.value.isBookmarked
            val newBookmarked = !currentlyBookmarked

            updateUiState { copy(isBookmarked = newBookmarked) }

            val article = _uiState.value.article
            if (newBookmarked) {
                insertArticleUseCase(article!!)
            } else {
                deleteArticleByUrlUseCase(article!!.url!!)
            }
        }
    }

    private fun openArticleInBrowser() {
        viewModelScope.launch {
            val url = _uiState.value.article!!.url!!
            _uiEffect.emit(UiEffect.OpenUrlInBrowser(url))
        }
    }

    private fun shareArticle() {
        viewModelScope.launch {
            val url = _uiState.value.article!!.url!!
            _uiEffect.emit(UiEffect.ShareArticle(url))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _uiEffect.emit(UiEffect.NavigateBack)
        }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }
}