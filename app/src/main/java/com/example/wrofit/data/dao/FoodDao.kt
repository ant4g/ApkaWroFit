package com.example.wrofit.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.wrofit.data.model.FoodEntry

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: FoodEntry)

    @Delete
    suspend fun delete(entry: FoodEntry)

    @Query("SELECT * FROM food_table ORDER BY id DESC")
    fun getAllEntries(): LiveData<List<FoodEntry>>

    @Query("SELECT IFNULL(SUM(calories), 0.0) FROM food_table")
    fun getTotalCalories(): LiveData<Double>
}