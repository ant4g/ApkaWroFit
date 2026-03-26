package com.example.wrofit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ExerciseUiState(
    val selectedDate: String = "",
    val checkedExercises: Set<String> = emptySet()
)

class ExerciseViewModel : ViewModel() {
    val exercises = listOf("Pajacyki", "Przysiady", "Pompki", "Deska", "Wykroki", "Burpees")

    var uiState by mutableStateOf(ExerciseUiState())
        private set

    fun setSelectedDate(date: String) {
        uiState = uiState.copy(selectedDate = date)
    }

    fun toggleExercise(name: String, isChecked: Boolean) {
        uiState = uiState.copy(
            checkedExercises = if (isChecked) {
                uiState.checkedExercises + name
            } else {
                uiState.checkedExercises - name
            }
        )
    }

    fun resetCheckedExercises() {
        uiState = uiState.copy(checkedExercises = emptySet())
    }
}
