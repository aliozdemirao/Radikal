package com.aliozdemir.radikal.domain.usecase

import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllBookmarkedArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<List<Article>> {
        return repository.getAllBookmarkedArticles()
    }
}