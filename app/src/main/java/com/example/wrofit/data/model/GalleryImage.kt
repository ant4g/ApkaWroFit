package com.example.wrofit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gallery_images")
data class GalleryImage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val drawableName: String
)
