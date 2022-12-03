package io.astronout.dicodingstoryapp.domain

import androidx.paging.PagingData
import androidx.paging.map
import io.astronout.dicodingstoryapp.data.source.DicodingStoryRepository
import io.astronout.dicodingstoryapp.domain.model.Story
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

class StoryInteractor @Inject constructor(private val repo: DicodingStoryRepository) : StoryUsecase {

    override fun getStories(): Flow<Resource<List<Story>>> {
        return repo.getStories()
    }

    override fun getAllStories(): Flow<PagingData<Story>> {
        return repo.getAllStories().map { data ->
            data.map { it.toStory() }
        }
    }

    override fun addNewStory(file: File, description: String, lat: String?, lon: String?): Flow<Resource<Unit>> {
        return repo.addNewStory(file, description, lat, lon)
    }

}