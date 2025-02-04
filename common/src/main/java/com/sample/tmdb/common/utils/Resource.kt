package com.sample.tmdb.common.utils

sealed class Resource<out R> {
    data object Loading : Resource<Nothing>()

    data class Success<out T>(val data: T) : Resource<T>()

    data class Error(val message: String) : Resource<Nothing>()
}
