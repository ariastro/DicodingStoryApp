package io.astronout.dicodingstoryapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.astronout.dicodingstoryapp.databinding.ItemStoryBinding
import io.astronout.dicodingstoryapp.domain.model.Story
import io.astronout.dicodingstoryapp.utils.setImageUrl
import io.astronout.dicodingstoryapp.utils.toDateString

class StoryAdapter(private val onStoryClicked: (Story, ItemStoryBinding) -> Unit): PagingDataAdapter<Story, StoryAdapter.ViewHolder>(
    DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(private val itemStoryBinding: ItemStoryBinding): RecyclerView.ViewHolder(itemStoryBinding.root) {
        fun bind(story: Story) {
            with(itemStoryBinding) {
                ivStoryImage.setImageUrl(story.photoUrl)
                tvUsername.text = story.name
                tvDescription.text = story.description
                tvDate.text = story.createdAt.toDateString()
                ivStoryImage.transitionName = story.id
                root.setOnClickListener {
                    onStoryClicked(story, itemStoryBinding)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean =
                oldItem == newItem

        }
    }

}