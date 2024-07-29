package com.rafiul.modernarchitectureapproach.repositories

import com.rafiul.modernarchitectureapproach.model.ResponseUsers
import com.rafiul.modernarchitectureapproach.sealed.AppError
import com.rafiul.modernarchitectureapproach.sealed.DataState
import com.rafiul.modernarchitectureapproach.services.UserApi
import com.rafiul.modernarchitectureapproach.utils.mapHttpError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val api: UserApi) : UserRepository {
    override suspend fun getAllUserFromRepo(): Flow<DataState<List<ResponseUsers>>> =
        flow {
            emit(DataState.Loading)
            try {
                val response = api.getAllUsers()
                if (response.isSuccessful) {
                    val responseBody = response.body() ?: emptyList()
                    if (responseBody.isEmpty()) {
                        emit(DataState.Empty)
                    } else {
                        emit(DataState.Success(responseBody))
                    }
                }else{
                    val errorBody = response.errorBody()?.string() ?: "Unknown Exception"
                    val appError = mapHttpError(response.code(), errorBody)
                    emit(DataState.Error(appError.toString()))
                }
            } catch (e: Exception) {
                val appError = when (e) {
                    is UnknownHostException -> AppError.NetworkError("Check Your Internet Connection")
                    is SocketTimeoutException -> AppError.TimeoutError
                    else -> AppError.UnknownError
                }
                emit(DataState.Error(appError.toString()))
            }
        }.retry(3) { throwable ->
            throwable is UnknownHostException || throwable is IOException
        }.flowOn(Dispatchers.IO)
}