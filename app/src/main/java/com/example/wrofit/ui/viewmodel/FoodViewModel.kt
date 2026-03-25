package com.example.wrofit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.wrofit.data.database.WroFitDatabase
import com.example.wrofit.data.model.FoodEntry
import com.example.wrofit.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository
    val allEntries: LiveData<List<FoodEntry>>
    val totalCalories: LiveData<Double>

    init {
        val dao = WroFitDatabase.getDatabase(application).foodDao()
        repository = FoodRepository(dao)
        allEntries = repository.allEntries
        totalCalories = repository.totalCalories
    }

    fun insert(entry: FoodEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entry)
    }

    fun delete(entry: FoodEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(entry)
    }
}