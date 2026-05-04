package com.example.wrofit.data.model

import androidx.room.Entity

@Entity(tableName = "food_day_table", primaryKeys = ["profileId", "date"])
data class FoodDayEntity(
    val profileId: String,
    val date: String,
    val breakfastJson: String,
    val lunchJson: String,
    val dinnerJson: String,
    val dailyCalorieGoal: String
)
