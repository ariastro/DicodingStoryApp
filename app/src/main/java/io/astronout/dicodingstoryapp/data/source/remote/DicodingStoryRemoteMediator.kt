package io.astronout.dicodingstoryapp.data.source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.skydoves.sandwich.onSuccess
import io.astronout.dicodingstoryapp.data.source.local.LocalDataSource
import io.astronout.dicodingstoryapp.data.source.local.entity.RemoteKeys
import io.astronout.dicodingstoryapp.data.source.local.entity.StoryEntity
import io.astronout.dicodingstoryapp.data.source.remote.model.StoriesResponse
import io.astronout.dicodingstoryapp.data.source.remote.web.DicodingStoryApi

@ExperimentalPagingApi
class DicodingStoryRemoteMediator(
    private val api: DicodingStoryApi,
    private val localDataSource: LocalDataSource
) : RemoteMediator<Int, StoryEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
            }
        }

        try {
            val responseData = api.getAllStories(page, state.config.pageSize)
            var endOfPaginationReached = false
            var stories: List<StoriesResponse.StoryResponse>? = null
            responseData.onSuccess {
                stories = response.body()?.listStory
                endOfPaginationReached = stories.isNullOrEmpty()
            }

            localDataSource.getDatabase().withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.clearRemoteKeys()
                    localDataSource.clearStories()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = stories?.map {
                    RemoteKeys(id = it.id.orEmpty(), prevKey = prevKey, nextKey = nextKey)
                }

                keys?.let {
                    localDataSource.insertRemoteKeys(it)
                }
                stories?.map { it.toStoryEntity() }?.let {
                    localDataSource.insertStories(it)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            localDataSource.getRemoteKeys(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            localDataSource.getRemoteKeys(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                localDataSource.getRemoteKeys(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}