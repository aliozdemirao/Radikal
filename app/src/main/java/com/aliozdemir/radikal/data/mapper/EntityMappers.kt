package com.aliozdemir.radikal.data.mapper

import com.aliozdemir.radikal.data.local.entity.ArticleEntity
import com.aliozdemir.radikal.data.local.entity.SourceEntity
import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.domain.model.Source

fun ArticleEntity.toDomain(): Article = Article(
    source = this.source?.toDomain(),
    author = this.author,
    title = this.title,
    description = this.description,
    url = this.url,
    urlToImage = this.urlToImage,
    publishedAt = this.publishedAt,
    content = this.content
)

fun Article.toEntity(): ArticleEntity = ArticleEntity(
    source = this.source?.toEntity(),
    author = this.author,
    title = this.title,
    description = this.description,
    url = this.url,
    urlToImage = this.urlToImage,
    publishedAt = this.publishedAt,
    content = this.content
)

fun SourceEntity.toDomain(): Source = Source(
    id = this.id,
    name = this.name
)

fun Source.toEntity(): SourceEntity = SourceEntity(
    id = this.id,
    name = this.name
)