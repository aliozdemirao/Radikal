package com.aliozdemir.radikal.domain.repository

import com.aliozdemir.radikal.domain.model.News
import com.aliozdemir.radikal.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getTopHeadlines(
        country: String?,
        category: String?,
        sources: String?,
        query: String?,
        pageSize: Int?,
        page: Int?
    ): Flow<Resource<News>>
}