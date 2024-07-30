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

    init {
        getAllUserFromVM()
    }

    private fun getAllUserFromVM() {
        viewModelScope.launch {
           userRepository.getAllUserFromRepo().collect{ state->
               _userViewState.value = when(state){
                  is DataState.Loading -> UiState.Loading
                   is DataState.Error -> UiState.Error(state.exception.getUserFriendlyMessage())
                   is DataState.Success -> UiState.Success(state.data)
                   else -> UiState.Empty("Wait For A While......")
               }

           }
        }
    }

//    private fun getAllUserFromVM() {
//        viewModelScope.launch {
//            _userViewState.value = DataState.Loading
//            try {
//                userRepository.getAllUserFromRepo().collect { state ->
//                    _userViewState.value = state
//                }
//            } catch (e: Exception) {
//                _userViewState.value = DataState.Error(e.toString())
//            }
//        }
//    }
}
