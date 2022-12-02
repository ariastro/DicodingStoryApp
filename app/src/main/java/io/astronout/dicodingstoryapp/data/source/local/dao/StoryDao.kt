package io.astronout.dicodingstoryapp.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.astronout.dicodingstoryapp.data.source.local.entity.StoryEntity

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<StoryEntity>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story")
    fun deleteAllStories()
}