package com.aliozdemir.radikal.domain.usecase

import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.domain.repository.NewsRepository
import javax.inject.Inject

class InsertArticleUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(article: Article) {
        repository.insertArticle(article)
    }
}