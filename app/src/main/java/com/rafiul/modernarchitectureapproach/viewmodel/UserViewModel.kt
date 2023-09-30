package com.rafiul.modernarchitectureapproach.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafiul.modernarchitectureapproach.model.ResponseUsers
import com.rafiul.modernarchitectureapproach.repositories.UserRepository
import com.rafiul.modernarchitectureapproach.sealed.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {


    private val _userViewState = MutableStateFlow<DataState<List<ResponseUsers>>>(DataState.Loading)
    val userViewState: StateFlow<DataState<List<ResponseUsers>>> = _userViewState


    fun getAllUserFromVM() {
        viewModelScope.launch {
            try {
                val response = userRepository.getAllUserFromRepo()
                if (response.isSuccessful) {
                    val userList = response.body() ?: emptyList()
                    _userViewState.value = DataState.Success(userList)
                } else {
                    _userViewState.value = DataState.Error(Exception("Error Fetching Values....."))
                }
            } catch (e: Exception) {
                _userViewState.value = DataState.Error(e)
            }
        }
    }

}