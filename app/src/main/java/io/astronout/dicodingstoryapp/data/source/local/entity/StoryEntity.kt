package io.astronout.dicodingstoryapp.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.astronout.dicodingstoryapp.domain.model.Story
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    @ColumnInfo(name = "photo_url")
    val photoUrl: String,
    @ColumnInfo(name = "lon")
    val lon: Double,
    @ColumnInfo(name = "lat")
    val lat: Double
) : Parcelable {
    fun toStory(): Story {
        return Story(
            id = id,
            name = name,
            description = description,
            createdAt = createdAt,
            photoUrl = photoUrl,
            lon = lon,
            lat = lat
        )
    }
}
