package com.aliozdemir.radikal.domain.usecase

import com.aliozdemir.radikal.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteArticleByUrlUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(url: String) {
        repository.deleteArticleByUrl(url)
    }
}