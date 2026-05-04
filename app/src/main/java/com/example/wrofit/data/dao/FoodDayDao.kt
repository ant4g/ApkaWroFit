package com.example.wrofit.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wrofit.data.model.FoodDayEntity

@Dao
interface FoodDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(day: FoodDayEntity)

    @Query("SELECT * FROM food_day_table WHERE profileId = :profileId AND date = :date LIMIT 1")
    suspend fun getDay(profileId: String, date: String): FoodDayEntity?

    @Query("DELETE FROM food_day_table WHERE profileId = :profileId")
    suspend fun deleteForProfile(profileId: String)
}
