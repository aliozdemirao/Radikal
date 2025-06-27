package com.aliozdemir.radikal.domain.usecase

import com.aliozdemir.radikal.domain.model.News
import com.aliozdemir.radikal.domain.repository.NewsRepository
import com.aliozdemir.radikal.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEverythingUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    operator fun invoke(
        query: String?,
        searchIn: String?,
        sources: String?,
        domains: String?,
        excludeDomains: String?,
        from: String?,
        to: String?,
        language: String?,
        sortBy: String?,
        pageSize: Int?,
        page: Int?,
    ): Flow<Resource<News>> {
        return repository.getEverything(
            query = query,
            searchIn = searchIn,
            sources = sources,
            domains = domains,
            excludeDomains = excludeDomains,
            from = from,
            to = to,
            language = language,
            sortBy = sortBy,
            pageSize = pageSize,
            page = page
        )
    }
}