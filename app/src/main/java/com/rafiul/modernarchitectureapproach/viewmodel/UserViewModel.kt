package com.rafiul.modernarchitectureapproach.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafiul.modernarchitectureapproach.model.ResponseUsers
import com.rafiul.modernarchitectureapproach.repositories.UserRepository
import com.rafiul.modernarchitectureapproach.sealed.DataState
import com.rafiul.modernarchitectureapproach.sealed.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _userViewState = MutableStateFlow<UiState<List<ResponseUsers>>>(UiState.Loading)
    val userViewState: StateFlow<UiState<List<ResponseUsers>>> = _userViewState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        getAllUserFromVM()
    }

    private fun getAllUserFromVM() {
        viewModelScope.launch {
           userRepository.getAllUserFromRepo().collect{ state->
               handleUiState(state)
           }
        }
    }

    fun refreshUsers() {
        _isRefreshing.value = true
        viewModelScope.launch {
            userRepository.getAllUserFromRepo().collect { state ->
                _isRefreshing.value = false
                handleUiState(state)
            }
        }
    }

    private fun handleUiState(state: DataState<List<ResponseUsers>>) {
        _userViewState.value = when (state) {
            is DataState.Loading -> UiState.Loading
            is DataState.Error -> UiState.Error(state.exception.getUserFriendlyMessage())
            is DataState.Success -> {
                if (state.data.isEmpty()) {
                    UiState.Empty("No users available")
                } else {
                    UiState.Success(state.data)
                }
            }
            else -> UiState.Empty("Wait For A While......")
        }
    }



}
