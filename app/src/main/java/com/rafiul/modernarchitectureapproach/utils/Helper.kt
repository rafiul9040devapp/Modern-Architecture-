package com.rafiul.modernarchitectureapproach.utils

import com.rafiul.modernarchitectureapproach.sealed.AppError

fun mapHttpError(code: Int, message: String): AppError {
    return when (code) {
        401 -> AppError.UnauthorizedError
        403 -> AppError.ForbiddenError
        404 -> AppError.NotFoundError
        500 -> AppError.InternalServerError
        else -> AppError.HttpError(code, message)
    }
}
