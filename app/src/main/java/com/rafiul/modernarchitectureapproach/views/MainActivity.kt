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
import com.rafiul.modernarchitectureapproach.sealed.DataState
import com.rafiul.modernarchitectureapproach.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.tvUsername.text = viewModel.getAllUserFromVM().toString()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userViewState.collect {
                    when (it) {
                        is DataState.Loading -> {
                            binding.progress.visibility = View.VISIBLE
                        }
                        is DataState.Success -> {
                            binding.progress.visibility = View.GONE
                            val users = it.data
                            Log.d("TAG", "onCreate: $users")
                        }
                        is DataState.Error -> {
                            val errorMessage = it.exception.message
                        }
                    }
                }
            }
        }

    }
}