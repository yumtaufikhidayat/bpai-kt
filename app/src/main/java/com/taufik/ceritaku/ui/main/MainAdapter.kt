package com.taufik.ceritaku.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.ceritaku.databinding.ItemListStoriesBinding
import com.taufik.ceritaku.ui.detail.DetailActivity
import com.taufik.ceritaku.ui.main.data.ListStoryItem
import com.taufik.ceritaku.utils.common.CommonConstant
import com.taufik.ceritaku.utils.common.CommonExtension.formatDate
import com.taufik.ceritaku.utils.common.CommonExtension.loadImage
import com.taufik.ceritaku.utils.common.CommonExtension.parseDate
import java.util.*

class MainAdapter: ListAdapter<ListStoryItem, MainAdapter.MainViewHolder>(MainDiffCallback), Filterable {

    private var listStories = listOf<ListStoryItem>()

    fun setData(list: List<ListStoryItem>) {
        this.listStories = list
        submitList(list)
    }

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

            constraintListStories.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_DATA, data)
                }
                it.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }

    override fun getFilter(): Filter = searchFilter

    private val searchFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence): FilterResults {
            val filteredList = mutableListOf<ListStoryItem>()
            if (p0.isEmpty()) {
                filteredList.addAll(listStories)
            } else {
                val filterPattern = p0.toString().lowercase(Locale.ROOT).trim()
                for (item in listStories) {
                    if (item.name.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            val result = FilterResults()
            result.values = filteredList
            return result
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<ListStoryItem>?)
        }
    }
}