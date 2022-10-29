package io.astronout.dicodingstoryapp.domain

import io.astronout.dicodingstoryapp.data.source.DicodingStoryRepository
import io.astronout.dicodingstoryapp.domain.model.Story
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoryInteractor @Inject constructor(private val repo: DicodingStoryRepository) : StoryUsecase {

    override fun getAllStories(): Flow<Resource<List<Story>>> {
        return repo.getAllStories()
    }

}