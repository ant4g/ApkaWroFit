package com.example.wrofit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wrofit.data.dao.FoodDao
import com.example.wrofit.data.model.FoodEntry

@Database(entities = [FoodEntry::class], version = 1, exportSchema = false)
abstract class WroFitDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao

    companion object {
        @Volatile
        private var INSTANCE: WroFitDatabase? = null

        fun getDatabase(context: Context): WroFitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WroFitDatabase::class.java,
                    "wrofit_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}