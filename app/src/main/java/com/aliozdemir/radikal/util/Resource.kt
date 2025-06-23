package com.aliozdemir.radikal.util

sealed class Resource<out T> {

    data class Success<out T>(val data: T) : Resource<T>()

    data class Error(
        val message: String,
        val errorCode: String? = null,
        val throwable: Throwable? = null,
    ) : Resource<Nothing>()

    data object Loading : Resource<Nothing>()
}