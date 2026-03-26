package com.example.wrofit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wrofit.data.dao.FoodDao
import com.example.wrofit.data.dao.GalleryImageDao
import com.example.wrofit.data.model.FoodEntry
import com.example.wrofit.data.model.GalleryImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [FoodEntry::class, GalleryImage::class], version = 2, exportSchema = false)
abstract class WroFitDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun galleryImageDao(): GalleryImageDao

    companion object {
        @Volatile
        private var INSTANCE: WroFitDatabase? = null

        fun getDatabase(context: Context): WroFitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WroFitDatabase::class.java,
                    "wrofit_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = instance.galleryImageDao()
                    if (dao.count() == 0) {
                        dao.insertAll(defaultGalleryImages())
                    }
                }
                instance
            }
        }

        private fun defaultGalleryImages(): List<GalleryImage> {
            return listOf(
                GalleryImage(title = "Burpees", drawableName = "home_position_1"),
                GalleryImage(title = "Deska", drawableName = "home_position_2"),
                GalleryImage(title = "Mostek biodrowy", drawableName = "home_position_3"),
                GalleryImage(title = "Pajacyki", drawableName = "home_position_4"),
                GalleryImage(title = "Pompki", drawableName = "home_position_5"),
                GalleryImage(title = "Przysiad szeroki", drawableName = "home_position_6"),
                GalleryImage(title = "Przysiady", drawableName = "home_position_7"),
                GalleryImage(title = "Rowerek", drawableName = "home_position_8"),
                GalleryImage(title = "Russian twist", drawableName = "home_position_9"),
                GalleryImage(title = "Superman", drawableName = "home_position_10"),
                GalleryImage(title = "Wspinaczka", drawableName = "home_position_11"),
                GalleryImage(title = "Trucht w miejscu", drawableName = "home_position_12"),
                GalleryImage(title = "Wykroki", drawableName = "home_position_13"),
                GalleryImage(title = "Wznoszenie nóg", drawableName = "home_position_14")
            )
        }
    }
}
