package com.example.wrofit.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wrofit.data.model.SleepDayEntity

@Dao
interface SleepDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(day: SleepDayEntity)

    @Query("SELECT * FROM sleep_day_table WHERE profileId = :profileId AND date = :date LIMIT 1")
    suspend fun getDay(profileId: String, date: String): SleepDayEntity?

    @Query("DELETE FROM sleep_day_table WHERE profileId = :profileId")
    suspend fun deleteForProfile(profileId: String)
}
