package io.astronout.dicodingstoryapp.ui.addstory

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.astronout.dicodingstoryapp.domain.StoryUsecase
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(private val storyUsecase: StoryUsecase) : ViewModel() {

    fun addNewStory(file: File, description: String) = storyUsecase.addNewStory(file, description)

}