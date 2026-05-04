package com.example.wrofit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class ProfileEntity(
    @PrimaryKey
    val profileId: String,
    val selectedDate: String,
    val profilePhotoUri: String,
    val fullName: String,
    val gender: String,
    val weight: String,
    val height: String,
    val goal: String,
    val activityLevel: String,
    val isProfileSaved: Boolean
)
