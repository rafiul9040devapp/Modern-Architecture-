package com.rafiul.modernarchitectureapproach.repositories

import com.rafiul.modernarchitectureapproach.model.ResponseUsers
import com.rafiul.modernarchitectureapproach.sealed.DataState
import kotlinx.coroutines.flow.Flow

interface UserRepository  {
    suspend fun getAllUserFromRepo(): Flow<DataState<List<ResponseUsers>>>
}