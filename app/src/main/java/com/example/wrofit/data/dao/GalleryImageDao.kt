package com.example.wrofit.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wrofit.data.model.GalleryImage

@Dao
interface GalleryImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GalleryImage>)

    @Query("SELECT * FROM gallery_images ORDER BY id ASC")
    fun getAllImages(): LiveData<List<GalleryImage>>

    @Query("SELECT COUNT(*) FROM gallery_images")
    suspend fun count(): Int
}
