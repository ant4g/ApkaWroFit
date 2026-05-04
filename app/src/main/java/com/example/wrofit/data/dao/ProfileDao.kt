package com.example.wrofit.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wrofit.data.model.ProfileEntity

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: ProfileEntity)

    @Query("SELECT * FROM profile_table LIMIT 1")
    suspend fun getSavedProfile(): ProfileEntity?

    @Query("DELETE FROM profile_table")
    suspend fun deleteAll()
}
