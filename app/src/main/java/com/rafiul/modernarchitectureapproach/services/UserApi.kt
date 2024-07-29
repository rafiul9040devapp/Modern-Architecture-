package com.rafiul.modernarchitectureapproach.services

import com.rafiul.modernarchitectureapproach.model.ResponseUsers
import com.rafiul.modernarchitectureapproach.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET(Constants.USER_END_POINT)
    suspend fun getAllUsers(): Response<List<ResponseUsers>>
}