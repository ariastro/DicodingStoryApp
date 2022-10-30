package io.astronout.dicodingstoryapp.domain

import io.astronout.dicodingstoryapp.domain.model.Story
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryUsecase {

    fun getAllStories(): Flow<Resource<List<Story>>>

    fun addNewStory(file: File, description: String): Flow<Resource<Unit>>
}