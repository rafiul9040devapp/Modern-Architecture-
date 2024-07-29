package com.rafiul.modernarchitectureapproach.sealed

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val errorMessage: String) : UiState<Nothing>()
    data class Empty(val message: String) : UiState<Nothing>()
}
