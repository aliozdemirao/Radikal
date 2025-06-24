package com.aliozdemir.radikal.di

import android.content.Context
import androidx.room.Room
import com.aliozdemir.radikal.data.local.dao.ArticleDao
import com.aliozdemir.radikal.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "radikal_db"
        ).build()

    @Provides
    @Singleton
    fun provideArticleDao(database: AppDatabase): ArticleDao = database.articleDao()
}