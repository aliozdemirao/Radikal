package com.aliozdemir.radikal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles",
    indices = [Index(value = ["url"], unique = true)],
)
data class ArticleEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @Embedded
    val source: SourceEntity?,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "url_to_image")
    val urlToImage: String?,

    @ColumnInfo(name = "published_at")
    val publishedAt: String?,

    @ColumnInfo(name = "content")
    val content: String?,
)