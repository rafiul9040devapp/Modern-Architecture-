package com.rafiul.modernarchitectureapproach.sealed

import java.lang.Exception

sealed class DataState<out T> {
    data object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
}
