package com.rafiul.modernarchitectureapproach.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafiul.modernarchitectureapproach.databinding.ItemShimmerPlaceholderBinding

class ShimmerAdapter(private val itemCount:Int):RecyclerView.Adapter<ShimmerAdapter.ShimmerViewHolder>() {

    inner class ShimmerViewHolder(private val binding: ItemShimmerPlaceholderBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemShimmerPlaceholderBinding.inflate(inflater,parent,false)
        return ShimmerViewHolder(binding)
    }

    override fun getItemCount(): Int = itemCount

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {}
}