package io.astronout.dicodingstoryapp.ui.home

import androidx.paging.PagingData
import io.astronout.dicodingstoryapp.databinding.ItemStoryBinding
import io.astronout.dicodingstoryapp.domain.model.Story

interface HomeContract {
    fun setupAdapter()
    fun setupMenu()
    fun onGetAllStoriesLoading()
    fun onGetAllStoriesSuccess(data: PagingData<Story>)
    fun onGetAllStoriesFailed(message: String)
    fun onNavigateToDetailStory(story: Story, itemStoryBinding: ItemStoryBinding)
}