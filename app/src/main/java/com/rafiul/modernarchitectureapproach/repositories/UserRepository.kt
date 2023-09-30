package com.rafiul.modernarchitectureapproach.repositories

import com.rafiul.modernarchitectureapproach.model.ResponseUsers
import com.rafiul.modernarchitectureapproach.services.UserService
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {
    suspend fun getAllUserFromRepo(): Response<List<ResponseUsers>> {
        return userService.getAllUsers()
    }
}