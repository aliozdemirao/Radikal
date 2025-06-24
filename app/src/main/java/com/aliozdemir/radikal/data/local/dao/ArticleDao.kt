package com.aliozdemir.radikal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aliozdemir.radikal.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert
    suspend fun insertArticle(article: ArticleEntity)

    @Query("DELETE FROM articles WHERE url = :articleUrl")
    suspend fun deleteArticleByUrl(articleUrl: String)

    @Query("SELECT EXISTS(SELECT 1 FROM articles WHERE url = :articleUrl LIMIT 1)")
    suspend fun isArticleBookmarked(articleUrl: String): Boolean

    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getAllBookmarkedArticles(): Flow<List<ArticleEntity>>

    @Query("DELETE FROM articles")
    suspend fun deleteAllBookmarkedArticles()
}