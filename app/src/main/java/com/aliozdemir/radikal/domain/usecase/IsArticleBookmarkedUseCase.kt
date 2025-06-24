package com.aliozdemir.radikal.domain.usecase

import com.aliozdemir.radikal.domain.repository.NewsRepository
import javax.inject.Inject

class IsArticleBookmarkedUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(articleUrl: String): Boolean {
        return repository.isArticleBookmarked(articleUrl)
    }
}