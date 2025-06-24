package com.aliozdemir.radikal.data.repository

import com.aliozdemir.radikal.data.local.dao.ArticleDao
import com.aliozdemir.radikal.data.mapper.toDomain
import com.aliozdemir.radikal.data.mapper.toEntity
import com.aliozdemir.radikal.data.remote.api.NewsApi
import com.aliozdemir.radikal.data.remote.dto.ErrorDto
import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.domain.model.News
import com.aliozdemir.radikal.domain.repository.NewsRepository
import com.aliozdemir.radikal.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val dao: ArticleDao,
) : NewsRepository {

    override fun getTopHeadlines(
        country: String?,
        category: String?,
        sources: String?,
        query: String?,
        pageSize: Int?,
        page: Int?,
    ): Flow<Resource<News>> = flow {
        emit(Resource.Loading)

        try {
            val newsDto = api.getTopHeadlines(
                country = country,
                category = category,
                sources = sources,
                query = query,
                pageSize = pageSize,
                page = page
            )

            emit(Resource.Success(newsDto.toDomain()))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()
            val (apiErrorMessage, apiErrorCode) = parseErrorBody(errorBody)

            emit(
                Resource.Error(
                    message = apiErrorMessage ?: "An unexpected HTTP error occurred: ${e.code()}",
                    errorCode = apiErrorCode,
                    throwable = e
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Network connection error: Please check your internet connection.",
                    throwable = e
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unknown error occurred.",
                    throwable = e
                )
            )
        }
    }

    override suspend fun insertArticle(article: Article) {
        dao.insertArticle(article.toEntity())
    }

    override suspend fun deleteArticleByUrl(articleUrl: String) {
        dao.deleteArticleByUrl(articleUrl)
    }

    override suspend fun isArticleBookmarked(articleUrl: String): Boolean {
        return dao.isArticleBookmarked(articleUrl)
    }

    override fun getAllBookmarkedArticles(): Flow<List<Article>> {
        return dao.getAllBookmarkedArticles().map { articles ->
            articles.map { it.toDomain() }
        }
    }

    override suspend fun deleteAllBookmarkedArticles() {
        dao.deleteAllBookmarkedArticles()
    }

    private fun parseErrorBody(errorBody: ResponseBody?): Pair<String?, String?> {
        return try {
            val errorJsonString = errorBody?.string()

            if (!errorJsonString.isNullOrEmpty()) {
                val errorDto = Json.decodeFromString<ErrorDto>(errorJsonString)
                Pair(errorDto.message, errorDto.code)
            } else {
                Pair(null, null)
            }
        } catch (e: Exception) {
            Pair("The error details returned from the API could not be resolved.", null)
        } finally {
            errorBody?.close()
        }
    }
}