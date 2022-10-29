package io.astronout.dicodingstoryapp.ui.home

import io.astronout.dicodingstoryapp.databinding.ItemStoryBinding
import io.astronout.dicodingstoryapp.domain.model.Story

interface HomeContract {
    fun onGetAllStoriesLoading()
    fun onGetAllStoriesSuccess(data: List<Story>)
    fun onGetAllStoriesFailed(message: String)
    fun onNavigateToDetailStory(story: Story, itemStoryBinding: ItemStoryBinding)
}