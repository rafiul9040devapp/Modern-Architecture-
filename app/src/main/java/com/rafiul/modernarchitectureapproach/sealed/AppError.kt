package com.rafiul.modernarchitectureapproach.sealed

sealed class AppError {
    // General network errors
    data class NetworkError(val message: String) : AppError()

    // HTTP errors with status code and message
    data class HttpError(val code: Int, val message: String) : AppError()

    // Specific HTTP error cases
    object UnauthorizedError : AppError() // 401 Unauthorized
    object ForbiddenError : AppError()     // 403 Forbidden
    object NotFoundError : AppError()      // 404 Not Found
    object InternalServerError : AppError() // 500 Internal Server Error

    // Timeout and unknown errors
    object TimeoutError : AppError()
    object UnknownError : AppError()

    // Utility function to provide user-friendly messages based on error type
    fun getUserFriendlyMessage(): String {
        return when (this) {
            is NetworkError -> "Please check your internet connection."
            is HttpError -> handleHttpError()
            is TimeoutError -> "The request timed out. Please try again."
            is UnknownError -> "An unknown error occurred. Please try again."
            is UnauthorizedError -> "Unauthorized access. Please log in again."
            is ForbiddenError -> "You do not have permission to access this resource."
            is NotFoundError -> "The requested resource could not be found."
            is InternalServerError -> "An internal server error occurred. Please try again later."
        }
    }

    private fun HttpError.handleHttpError(): String {
        return when (code) {
            401 -> "Unauthorized access. Please log in again."
            403 -> "You do not have permission to access this resource."
            404 -> "The requested resource could not be found."
            500 -> "An internal server error occurred. Please try again later."
            else -> message
        }
    }
}
