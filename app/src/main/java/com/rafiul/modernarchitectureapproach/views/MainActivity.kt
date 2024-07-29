package com.rafiul.modernarchitectureapproach.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rafiul.modernarchitectureapproach.R
import com.rafiul.modernarchitectureapproach.databinding.ActivityMainBinding
import com.rafiul.modernarchitectureapproach.model.ResponseUsers
import com.rafiul.modernarchitectureapproach.sealed.DataState
import com.rafiul.modernarchitectureapproach.sealed.UiState
import com.rafiul.modernarchitectureapproach.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUserObserver()
    }

    private fun setUpUserObserver() {
        lifecycleScope.launch {
            viewModel.userViewState.collect{ state->
                when(state){
                    is UiState.Empty -> handlingState(state.message)
                    is UiState.Error -> handlingState(state.errorMessage)
                    UiState.Loading -> showLoading(true)
                    is UiState.Success -> handlingState(state.data.toString())
                }
            }
        }
    }

    private fun handlingState(state: String) {
        showLoading(false)
        binding.apply {
            tvUsername.visibility = View.VISIBLE
            tvUsername.text = state
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}