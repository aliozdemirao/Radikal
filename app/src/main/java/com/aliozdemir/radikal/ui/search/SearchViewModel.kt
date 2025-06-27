package com.aliozdemir.radikal.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.domain.usecase.GetEverythingUseCase
import com.aliozdemir.radikal.ui.search.SearchContract.FilterOptions
import com.aliozdemir.radikal.ui.search.SearchContract.UiAction
import com.aliozdemir.radikal.ui.search.SearchContract.UiEffect
import com.aliozdemir.radikal.ui.search.SearchContract.UiState
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getEverythingUseCase: GetEverythingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    fun handleUiAction(action: UiAction) {
        when (action) {
            UiAction.OnSearchClicked -> executeSearch()
            is UiAction.OnSearchQueryChanged -> updateSearchQuery(action.query)
            is UiAction.OnArticleClicked -> navigateToDetail(action.article)

            UiAction.OnFilterIconClicked -> updateFilterDialogVisibility(true)
            UiAction.OnFilterDialogDismissed -> updateFilterDialogVisibility(false)
            is UiAction.OnFilterApplied -> applyFilters(action.newFilterOptions)
            UiAction.OnClearFiltersClicked -> clearFilters()
        }
    }

    private fun updateSearchQuery(query: String) {
        updateUiState { copy(searchQuery = query) }
    }

    private fun updateFilterDialogVisibility(visible: Boolean) {
        updateUiState { copy(showFilterDialog = visible) }
    }

    private fun executeSearch() {
        val query = _uiState.value.searchQuery.trim()
        if (query.isBlank()) {
            updateUiState { copy(searchResults = emptyList(), isLoading = false, error = null) }
            return
        }

        val currentFilterOptions = _uiState.value.filterOptions

        getEverythingUseCase(
            query = query,
            searchIn = currentFilterOptions.searchIn?.joinToString(","),
            sources = currentFilterOptions.sources,
            domains = currentFilterOptions.domains,
            excludeDomains = currentFilterOptions.excludeDomains,
            from = currentFilterOptions.fromDate,
            to = currentFilterOptions.toDate,
            language = currentFilterOptions.language,
            sortBy = currentFilterOptions.sortBy,
            pageSize = null,
            page = null
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    updateUiState { copy(isLoading = true) }
                }

                is Resource.Success -> {
                    updateUiState {
                        copy(
                            searchResults = result.data.articles?.filterNotNull() ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is Resource.Error -> {
                    updateUiState {
                        copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun applyFilters(newFilterOptions: FilterOptions) {
        updateUiState { copy(filterOptions = newFilterOptions, showFilterDialog = false) }
        executeSearch()
    }

    private fun clearFilters() {
        updateUiState { copy(filterOptions = FilterOptions(), showFilterDialog = false) }
        executeSearch()
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