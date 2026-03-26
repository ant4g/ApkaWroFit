package com.example.wrofit.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.wrofit.data.database.WroFitDatabase
import com.example.wrofit.data.model.FoodEntry
import com.example.wrofit.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class FoodUiState(
    val selectedDate: String = "",
    val breakfastExpanded: Boolean = true,
    val lunchExpanded: Boolean = false,
    val dinnerExpanded: Boolean = false,
    val breakfastCalories: List<String> = listOf("", "", ""),
    val lunchCalories: List<String> = listOf("", "", ""),
    val dinnerCalories: List<String> = listOf("", "", "")
)

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository
    val allEntries: LiveData<List<FoodEntry>>
    val totalCalories: LiveData<Double>
    private val uiStateState: MutableState<FoodUiState> = mutableStateOf(FoodUiState())
    val uiState: FoodUiState
        get() = uiStateState.value

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

    fun setSelectedDate(date: String) {
        uiStateState.value = uiState.copy(selectedDate = date)
    }

    fun toggleBreakfast() {
        uiStateState.value = uiState.copy(breakfastExpanded = !uiState.breakfastExpanded)
    }

    fun toggleLunch() {
        uiStateState.value = uiState.copy(lunchExpanded = !uiState.lunchExpanded)
    }

    fun toggleDinner() {
        uiStateState.value = uiState.copy(dinnerExpanded = !uiState.dinnerExpanded)
    }

    fun setBreakfastCalories(index: Int, value: String) {
        uiStateState.value = uiState.copy(breakfastCalories = uiState.breakfastCalories.updatedAt(index, value))
    }

    fun setLunchCalories(index: Int, value: String) {
        uiStateState.value = uiState.copy(lunchCalories = uiState.lunchCalories.updatedAt(index, value))
    }

    fun setDinnerCalories(index: Int, value: String) {
        uiStateState.value = uiState.copy(dinnerCalories = uiState.dinnerCalories.updatedAt(index, value))
    }

    fun breakfastTotal(): Int = caloriesTotal(uiState.breakfastCalories)

    fun lunchTotal(): Int = caloriesTotal(uiState.lunchCalories)

    fun dinnerTotal(): Int = caloriesTotal(uiState.dinnerCalories)

    fun overallTotal(): Int = breakfastTotal() + lunchTotal() + dinnerTotal()

    private fun List<String>.updatedAt(index: Int, value: String): List<String> {
        return mapIndexed { currentIndex, currentValue ->
            if (currentIndex == index) value else currentValue
        }
    }

    private fun caloriesTotal(values: List<String>): Int {
        return values.sumOf { value -> value.toIntOrNull() ?: 0 }
    }
}
