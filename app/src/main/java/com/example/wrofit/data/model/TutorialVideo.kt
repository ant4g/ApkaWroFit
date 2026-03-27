package com.example.wrofit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tutorial_videos")
data class TutorialVideo(
    @PrimaryKey
    val id: String,
    val title: String,
    val resourceName: String
)
