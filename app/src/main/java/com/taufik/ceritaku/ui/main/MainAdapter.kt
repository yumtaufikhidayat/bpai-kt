package com.taufik.ceritaku.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.ceritaku.databinding.ItemListStoriesBinding
import com.taufik.ceritaku.ui.main.data.ListStoryItem
import com.taufik.ceritaku.utils.common.CommonConstant
import com.taufik.ceritaku.utils.common.CommonExtension.formatDate
import com.taufik.ceritaku.utils.common.CommonExtension.loadImage
import com.taufik.ceritaku.utils.common.CommonExtension.parseDate

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

            val date = data.createdAt
            val dateParse = date.parseDate(CommonConstant.DATE_YYYY_MM_DD_FORMAT)
            val dateFormat = dateParse.formatDate(CommonConstant.DATE_DD_MMMM_YYYY_FORMAT)
            tvCreatedAt.text = dateFormat

            tvDescription.text = data.description
        }
    }
}