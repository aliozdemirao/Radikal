package com.aliozdemir.radikal.data.remote.api

import com.aliozdemir.radikal.data.remote.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("sources") sources: String?,
        @Query("q") query: String?,
        @Query("pageSize") pageSize: Int?,
        @Query("page") page: Int?,
    ): NewsDto
}