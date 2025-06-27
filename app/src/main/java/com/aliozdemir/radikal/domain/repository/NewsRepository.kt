package com.aliozdemir.radikal.domain.repository

import com.aliozdemir.radikal.domain.model.Article
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

    fun getEverything(
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
    ): Flow<Resource<News>>

    suspend fun insertArticle(article: Article)
    suspend fun deleteArticleByUrl(articleUrl: String)
    suspend fun isArticleBookmarked(articleUrl: String): Boolean
    fun getAllBookmarkedArticles(): Flow<List<Article>>
    suspend fun deleteAllBookmarkedArticles()
}