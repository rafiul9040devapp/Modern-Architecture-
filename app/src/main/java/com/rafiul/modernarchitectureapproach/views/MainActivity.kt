package com.rafiul.modernarchitectureapproach.views

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rafiul.modernarchitectureapproach.adapters.ShimmerAdapter
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
    private lateinit var shimmerAdapter: ShimmerAdapter
    private val viewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUserAdapter()
        setUpShimmerAdapter()
        swipeToRefresh()
        observeRefreshState()
        setUpUserObserver()
    }

    private fun setUpShimmerAdapter() {
        shimmerAdapter = ShimmerAdapter(15)
        binding.shimmerRecyclerView.adapter = shimmerAdapter
    }

    private fun observeRefreshState() {
        lifecycleScope.launch {
            viewModel.isRefreshing.collect { isRefreshing ->
                binding.swipeRefresh.isRefreshing = isRefreshing
            }
        }
    }

    private fun swipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshUsers()
        }
    }

    private fun setUpUserAdapter() {
        userAdapter = UserAdapter(object : UserAdapter.Listener {
            override fun getUserDetails(user: ResponseUsers) {
                showToast("Want To see Details of : ${user.name}")
            }
        })
        binding.userRec.adapter = userAdapter
    }

    private fun setUpUserObserver() {
        lifecycleScope.launch {
            viewModel.userViewState.collect { state ->
                when (state) {
                    is UiState.Empty -> handleState(state.message)
                    is UiState.Error -> handleState(state.errorMessage)
                    UiState.Loading -> showLoading(true)
                    is UiState.Success -> handleState(state.data)
                }
            }
        }
    }

    private inline fun <reified T> handleState(data: T) {
        showLoading(false)
        with(binding) {
            swipeRefresh.isRefreshing = false
            when (data) {
                is String -> {
                    tvUsername.apply {
                        visibility = View.VISIBLE
                        text = data
                    }
                    userRec.visibility = View.GONE
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.visibility = View.GONE
                }
                is List<*> -> {
                    if (data.isNotEmpty() && data.first() is ResponseUsers) {
                        userAdapter.submitList(data as List<ResponseUsers>)
                        userRec.visibility = View.VISIBLE
                        tvUsername.visibility = View.GONE
                    } else {
                        tvUsername.apply {
                            visibility = View.VISIBLE
                            text = "No users available"
                        }
                        userRec.visibility = View.GONE
                    }
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.visibility = View.GONE
                }
                else -> {
                    tvUsername.visibility = View.GONE
                    userRec.visibility = View.GONE
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                shimmerViewContainer.apply {
                    startShimmer()
                    visibility = View.VISIBLE
                }
                userRec.visibility = View.GONE
                tvUsername.visibility = View.GONE
            } else {
                shimmerViewContainer.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
            }
        }
    }

}