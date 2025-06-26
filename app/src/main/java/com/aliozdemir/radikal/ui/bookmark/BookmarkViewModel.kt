package com.aliozdemir.radikal.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.domain.usecase.DeleteAllBookmarkedArticlesUseCase
import com.aliozdemir.radikal.domain.usecase.DeleteArticleByUrlUseCase
import com.aliozdemir.radikal.domain.usecase.GetAllBookmarkedArticlesUseCase
import com.aliozdemir.radikal.ui.bookmark.BookmarkContract.UiAction
import com.aliozdemir.radikal.ui.bookmark.BookmarkContract.UiEffect
import com.aliozdemir.radikal.ui.bookmark.BookmarkContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getAllBookmarkedArticlesUseCase: GetAllBookmarkedArticlesUseCase,
    private val deleteArticleByUrlUseCase: DeleteArticleByUrlUseCase,
    private val deleteAllBookmarkedArticlesUseCase: DeleteAllBookmarkedArticlesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    init {
        loadBookmarkedArticles()
    }

    fun handleUiAction(action: UiAction) {
        when (action) {
            UiAction.LoadBookmarkedArticles -> loadBookmarkedArticles()
            is UiAction.OnDeleteArticleClicked -> deleteArticle(action.articleUrl)
            UiAction.OnDeleteAllArticlesClicked -> deleteAllArticles()
            is UiAction.OnArticleClicked -> navigateToDetail(action.article)
        }
    }

    private fun loadBookmarkedArticles() {
        getAllBookmarkedArticlesUseCase()
            .onEach { articles -> updateUiState { copy(bookmarkedArticles = articles) } }
            .launchIn(viewModelScope)
    }

    private fun deleteArticle(articleUrl: String) {
        viewModelScope.launch {
            deleteArticleByUrlUseCase(articleUrl)
        }
    }

    private fun deleteAllArticles() {
        viewModelScope.launch {
            deleteAllBookmarkedArticlesUseCase()
        }
    }

    private fun navigateToDetail(article: Article) {
        viewModelScope.launch {
            _uiEffect.emit(UiEffect.NavigateToDetail(article))
        }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }
}