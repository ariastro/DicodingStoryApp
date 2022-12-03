package io.astronout.dicodingstoryapp.ui.maps

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.astronout.dicodingstoryapp.domain.StoryUsecase
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val storyUsecase: StoryUsecase) : ViewModel() {

    val allStories = storyUsecase.getStories()

}