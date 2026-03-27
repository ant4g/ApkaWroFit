package com.example.wrofit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wrofit.data.dao.FoodDao
import com.example.wrofit.data.dao.GalleryImageDao
import com.example.wrofit.data.dao.TutorialVideoDao
import com.example.wrofit.data.model.FoodEntry
import com.example.wrofit.data.model.GalleryImage
import com.example.wrofit.data.model.TutorialVideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

@Database(entities = [FoodEntry::class, GalleryImage::class, TutorialVideo::class], version = 3, exportSchema = false)
abstract class WroFitDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun galleryImageDao(): GalleryImageDao
    abstract fun tutorialVideoDao(): TutorialVideoDao

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
                    val galleryDao = instance.galleryImageDao()
                    val tutorialVideoDao = instance.tutorialVideoDao()

                    if (galleryDao.count() == 0) {
                        galleryDao.insertAll(loadDefaultGalleryImages(context))
                    }

                    if (tutorialVideoDao.count() == 0) {
                        tutorialVideoDao.insertAll(loadDefaultTutorialVideos(context))
                    }
                }
                instance
            }
        }

        private fun loadDefaultGalleryImages(context: Context): List<GalleryImage> {
            val json = context.assets.open("gallery_images.json").bufferedReader().use { it.readText() }
            val array = JSONArray(json)
            return buildList {
                for (index in 0 until array.length()) {
                    val item = array.getJSONObject(index)
                    add(
                        GalleryImage(
                            title = item.getString("title"),
                            drawableName = item.getString("drawableName")
                        )
                    )
                }
            }
        }

        private fun loadDefaultTutorialVideos(context: Context): List<TutorialVideo> {
            val json = context.assets.open("tutorial_videos.json").bufferedReader().use { it.readText() }
            val array = JSONArray(json)
            return buildList {
                for (index in 0 until array.length()) {
                    val item = array.getJSONObject(index)
                    add(
                        TutorialVideo(
                            id = item.getString("id"),
                            title = item.getString("title"),
                            resourceName = item.getString("resourceName")
                        )
                    )
                }
            }
        }
    }
}
