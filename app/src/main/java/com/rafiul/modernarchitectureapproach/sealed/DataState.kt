package com.rafiul.modernarchitectureapproach.sealed

sealed class DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: String) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
    data object Empty : DataState<Nothing>()
}

