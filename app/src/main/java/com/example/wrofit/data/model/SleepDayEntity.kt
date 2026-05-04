package com.example.wrofit.data.model

import androidx.room.Entity

@Entity(tableName = "sleep_day_table", primaryKeys = ["profileId", "date"])
data class SleepDayEntity(
    val profileId: String,
    val date: String,
    val sleepTime: String,
    val wakeTime: String,
    val difficulties: String,
    val sleepGoalHours: String
)
