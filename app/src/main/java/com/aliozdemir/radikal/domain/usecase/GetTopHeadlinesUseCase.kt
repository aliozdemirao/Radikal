package com.aliozdemir.radikal.domain.usecase

import com.aliozdemir.radikal.domain.model.News
import com.aliozdemir.radikal.domain.repository.NewsRepository
import com.aliozdemir.radikal.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    operator fun invoke(
        country: String?,
        category: String?,
        sources: String?,
        query: String?,
        pageSize: Int?,
        page: Int?,
    ): Flow<Resource<News>> {
        return repository.getTopHeadlines(
            country = country,
            category = category,
            sources = sources,
            query = query,
            pageSize = pageSize,
            page = page
        )
    }
}