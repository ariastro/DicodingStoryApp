package io.astronout.dicodingstoryapp.domain

import io.astronout.dicodingstoryapp.data.source.DicodingStoryRepository
import io.astronout.dicodingstoryapp.domain.model.Story
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class StoryInteractor @Inject constructor(private val repo: DicodingStoryRepository) : StoryUsecase {

    override fun getAllStories(): Flow<Resource<List<Story>>> {
        return repo.getAllStories()
    }

    override fun addNewStory(file: File, description: String): Flow<Resource<Unit>> {
        return repo.addNewStory(file, description)
    }

}