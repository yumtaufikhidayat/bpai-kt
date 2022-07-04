package com.taufik.ceritaku.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.ceritaku.databinding.ItemListStoriesBinding
import com.taufik.ceritaku.ui.main.data.ListStoryItem
import com.taufik.ceritaku.utils.Common.loadImage

class MainAdapter: ListAdapter<ListStoryItem, MainAdapter.MainViewHolder>(MainDiffCallback) {
    object MainDiffCallback: DiffUtil.ItemCallback<ListStoryItem>(){
        override fun areItemsTheSame(
            oldItem: ListStoryItem,
            newItem: ListStoryItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ListStoryItem,
            newItem: ListStoryItem
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(ItemListStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class MainViewHolder(private val binding: ItemListStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ListStoryItem) = with(binding) {
            imgStory.loadImage(data.photoUrl)
            tvName.text = data.name
            tvCreatedAt.text = data.createdAt
            tvDescription.text = data.description
        }
    }
}