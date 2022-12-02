package io.astronout.dicodingstoryapp.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.astronout.dicodingstoryapp.data.source.local.dao.RemoteKeysDao
import io.astronout.dicodingstoryapp.data.source.local.dao.StoryDao
import io.astronout.dicodingstoryapp.data.source.local.entity.RemoteKeys
import io.astronout.dicodingstoryapp.data.source.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class DicodingStoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}