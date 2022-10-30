package io.astronout.dicodingstoryapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.astronout.dicodingstoryapp.domain.AuthUsecase
import io.astronout.dicodingstoryapp.domain.StoryUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(storyUsecase: StoryUsecase, private val authUsecase: AuthUsecase) : ViewModel() {

    val allStories = storyUsecase.getAllStories()

    fun clearToken() {
        viewModelScope.launch {
            authUsecase.saveAuthToken("")
        }
    }

}