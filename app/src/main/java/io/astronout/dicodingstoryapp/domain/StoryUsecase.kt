package io.astronout.dicodingstoryapp.domain

import io.astronout.dicodingstoryapp.domain.model.Story
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.flow.Flow

interface StoryUsecase {

    fun getAllStories(): Flow<Resource<List<Story>>>
}