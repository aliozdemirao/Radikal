package com.aliozdemir.radikal.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliozdemir.radikal.domain.model.Article
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
class DetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    fun handleUiAction(action: UiAction) {
        when (action) {
            is UiAction.LoadArticleDetails -> loadArticle(action.article)
            UiAction.OnBrowserClicked -> openArticleInBrowser()
            UiAction.OnShareClicked -> shareArticle()
            UiAction.OnNavigateUpClicked -> navigateBack()
        }
    }

    private fun loadArticle(article: Article) {
        updateUiState { copy(article = article) }
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