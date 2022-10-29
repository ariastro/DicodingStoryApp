package io.astronout.dicodingstoryapp.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.astronout.dicodingstoryapp.domain.StoryUsecase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(storyUsecase: StoryUsecase) : ViewModel() {

    val allStories = storyUsecase.getAllStories()

}