package com.aliozdemir.radikal.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliozdemir.radikal.domain.usecase.GetTopHeadlinesUseCase
import com.aliozdemir.radikal.ui.home.HomeContract.UiAction
import com.aliozdemir.radikal.ui.home.HomeContract.UiEffect
import com.aliozdemir.radikal.ui.home.HomeContract.UiState
import com.aliozdemir.radikal.util.Resource
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    init {
        fetchNews()
    }

    fun handleUiAction(action: UiAction) {
        when (action) {
            is UiAction.OnCategorySelected -> updateSelectedCategory(action.category)
        }
    }

    private fun updateSelectedCategory(category: String) {
        if (_uiState.value.selectedCategory != category) {
            updateUiState { copy(selectedCategory = category) }
            fetchNews()
        }
    }

    private fun fetchNews() {
        val currentCategory = _uiState.value.selectedCategory
        val currentCountry = _uiState.value.selectedCountry

        getTopHeadlinesUseCase(
            country = currentCountry,
            category = currentCategory,
            sources = null,
            query = null,
            pageSize = null,
            page = null
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    updateUiState { copy(isLoading = true, error = null) }
                }

                is Resource.Success -> {
                    updateUiState {
                        copy(
                            isLoading = false,
                            articles = result.data.articles?.filterNotNull() ?: emptyList(),
                            error = null
                        )
                    }
                }

                is Resource.Error -> {
                    updateUiState { copy(isLoading = false, error = result.message) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }
}