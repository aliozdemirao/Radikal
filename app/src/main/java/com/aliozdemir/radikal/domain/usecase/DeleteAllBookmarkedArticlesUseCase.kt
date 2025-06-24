package com.aliozdemir.radikal.domain.usecase

import com.aliozdemir.radikal.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteAllBookmarkedArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllBookmarkedArticles()
    }
}