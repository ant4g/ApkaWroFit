package com.example.wrofit.repository

import androidx.lifecycle.LiveData
import com.example.wrofit.data.dao.FoodDao
import com.example.wrofit.data.model.FoodEntry

class FoodRepository(private val dao: FoodDao) {
    val allEntries: LiveData<List<FoodEntry>> = dao.getAllEntries()
    val totalCalories: LiveData<Double> = dao.getTotalCalories()

    suspend fun insert(entry: FoodEntry) = dao.insert(entry)
    suspend fun delete(entry: FoodEntry) = dao.delete(entry)
}