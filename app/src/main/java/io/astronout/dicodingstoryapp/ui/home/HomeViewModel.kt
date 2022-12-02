package io.astronout.dicodingstoryapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.astronout.dicodingstoryapp.domain.AuthUsecase
import io.astronout.dicodingstoryapp.domain.StoryUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(storyUsecase: StoryUsecase, private val authUsecase: AuthUsecase) : ViewModel() {

    val allStories = storyUsecase.getAllStories().cachedIn(viewModelScope).asLiveData()

    fun clearToken() {
        viewModelScope.launch {
            authUsecase.saveAuthToken("")
        }
    }

}