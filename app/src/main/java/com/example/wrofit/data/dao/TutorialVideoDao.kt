package com.example.wrofit.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wrofit.data.model.TutorialVideo

@Dao
interface TutorialVideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TutorialVideo>)

    @Query("SELECT * FROM tutorial_videos ORDER BY id ASC LIMIT 1")
    fun getFirstVideo(): LiveData<TutorialVideo?>

    @Query("SELECT COUNT(*) FROM tutorial_videos")
    suspend fun count(): Int
}
