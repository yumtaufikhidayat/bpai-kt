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
import com.taufik.ceritaku.data.local.entity.StoryEntity
import com.taufik.ceritaku.databinding.ItemListStoriesBinding
import com.taufik.ceritaku.ui.detail.DetailActivity
import com.taufik.ceritaku.utils.common.Common.formattedDate
import com.taufik.ceritaku.utils.common.CommonExtension.loadImage
import java.util.*

class MainAdapter: ListAdapter<StoryEntity, MainAdapter.MainViewHolder>(MainDiffCallback), Filterable {

    private var listStories = listOf<StoryEntity>()

    fun setData(list: List<StoryEntity>) {
        this.listStories = list
        submitList(list)
    }

    object MainDiffCallback: DiffUtil.ItemCallback<StoryEntity>(){
        override fun areItemsTheSame(
            oldItem: StoryEntity,
            newItem: StoryEntity
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: StoryEntity,
            newItem: StoryEntity
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
        fun onBind(data: StoryEntity) = with(binding) {
            imgStory.loadImage(data.photoUrl)
            tvName.text = data.name

            tvCreatedAt.text = formattedDate(data.createdAt, TimeZone.getDefault().id)

            tvDescription.text = data.description

            constraintListStories.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_DATA, data)
                }
                it.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity).toBundle())
            }
        }
    }

    override fun getFilter(): Filter = searchFilter

    private val searchFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence): FilterResults {
            val filteredList = mutableListOf<StoryEntity>()
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
            submitList(filterResults?.values as MutableList<StoryEntity>?)
        }
    }
}