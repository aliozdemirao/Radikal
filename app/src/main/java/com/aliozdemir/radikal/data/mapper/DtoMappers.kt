package com.aliozdemir.radikal.data.mapper

import com.aliozdemir.radikal.data.remote.dto.ArticleDto
import com.aliozdemir.radikal.data.remote.dto.NewsDto
import com.aliozdemir.radikal.data.remote.dto.SourceDto
import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.domain.model.News
import com.aliozdemir.radikal.domain.model.Source

fun SourceDto.toDomain(): Source {
    return Source(
        id = id,
        name = name
    )
}

fun ArticleDto.toDomain(): Article {
    return Article(
        source = source?.toDomain(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

fun NewsDto.toDomain(): News {
    return News(
        status = status,
        totalResults = totalResults,
        articles = articles?.map { it?.toDomain() }
    )
}