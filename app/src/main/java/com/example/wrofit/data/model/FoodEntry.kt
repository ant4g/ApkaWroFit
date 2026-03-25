package com.example.wrofit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class FoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mealName: String,     // np. "Owsianka"
    val calories: Double,     // wartość kaloryczna
    val mealType: String,     // Śniadanie, Obiad, Kolacja
    val date: String          // data posiłku [cite: 84]
)