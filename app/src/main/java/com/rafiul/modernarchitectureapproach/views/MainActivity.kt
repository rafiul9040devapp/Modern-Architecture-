package com.rafiul.modernarchitectureapproach.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rafiul.modernarchitectureapproach.adapters.UserAdapter
import com.rafiul.modernarchitectureapproach.databinding.ActivityMainBinding
import com.rafiul.modernarchitectureapproach.model.ResponseUsers
import com.rafiul.modernarchitectureapproach.sealed.UiState
import com.rafiul.modernarchitectureapproach.utils.showToast
import com.rafiul.modernarchitectureapproach.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private val viewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpAdapter()
        setUpUserObserver()
    }

    private fun setUpAdapter() {
        userAdapter = UserAdapter(object : UserAdapter.Listener {
            override fun getUserDetails(user: ResponseUsers) {
//                Toast.makeText(this@MainActivity,"Want To see Details of : ${user.name}" , Toast.LENGTH_SHORT).show()
                showToast("Want To see Details of : ${user.name}")
            }
        })
        binding.userRec.adapter = userAdapter
    }

    private fun setUpUserObserver() {
        lifecycleScope.launch {
            viewModel.userViewState.collect { state ->
                when (state) {
                    is UiState.Empty -> handlingState(state.message)
                    is UiState.Error -> handlingState(state.errorMessage)
                    UiState.Loading -> showLoading(true)
                    is UiState.Success -> handlingState(state.data)
                }
            }
        }
    }

    private inline fun <reified T> handlingState(data: T) {
        showLoading(false)
        with(binding) {
            when (data) {
                is String -> {
                    tvUsername.visibility = View.VISIBLE
                    tvUsername.text = data
                }

                is List<*> -> {
                    if (data.isNotEmpty() && data.first() is ResponseUsers) {
                        userAdapter.submitList(data as List<ResponseUsers>)
                        userRec.visibility = View.VISIBLE
                    }
                }

                else -> {

                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}