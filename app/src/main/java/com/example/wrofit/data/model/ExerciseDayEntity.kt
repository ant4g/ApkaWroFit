package com.example.wrofit.data.model

import androidx.room.Entity

@Entity(tableName = "exercise_day_table", primaryKeys = ["profileId", "date"])
data class ExerciseDayEntity(
    val profileId: String,
    val date: String,
    val checkedExercisesJson: String
)
