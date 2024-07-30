package com.rafiul.modernarchitectureapproach.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rafiul.modernarchitectureapproach.databinding.ItemUserViewHolderBinding
import com.rafiul.modernarchitectureapproach.model.ResponseUsers

class UserAdapter(private val listener: Listener) : ListAdapter<ResponseUsers, UserAdapter.UserViewHolder>(DiffCallback()) {

    interface Listener{
        fun getUserDetails(user: ResponseUsers)
    }

    inner class UserViewHolder(private val binding: ItemUserViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ResponseUsers) {
           with(binding){
               user.let {
                   nameTV.text = it.name
                   emailTV.text = it.email
               }

               itemView.setOnClickListener {
                   listener.getUserDetails(user)
               }
           }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater =  LayoutInflater.from(parent.context)
        val binding = ItemUserViewHolderBinding.inflate(inflater , parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class DiffCallback : DiffUtil.ItemCallback<ResponseUsers>() {
        override fun areItemsTheSame(oldItem: ResponseUsers, newItem: ResponseUsers): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResponseUsers, newItem: ResponseUsers): Boolean {
            return oldItem == newItem
        }
    }
}
