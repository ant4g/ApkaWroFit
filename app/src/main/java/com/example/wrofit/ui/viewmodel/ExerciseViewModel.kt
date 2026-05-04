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
<<<<<<< Updated upstream
    val exercises = listOf("Pajacyki", "Przysiady", "Pompki", "Deska", "Wykroki", "Burpees")
=======
    // Stala lista cwiczen pokazywana na ekranie aktywnosci.
    val exercises = listOf(
        "Pajacyki", "Przysiady", "Pompki", "Deska", "Wykroki",
        "Wspinaczka", "Mostek biodrowy", "Burpees", "Rowerek", "Unoszenie nóg"
    )

    // Dla kazdego dnia przechowujemy zbior zaznaczonych cwiczen.
    private val _exerciseHistory = mutableStateMapOf<String, Set<String>>()
    private var activeProfileId: String = ""
>>>>>>> Stashed changes

    var uiState by mutableStateOf(ExerciseUiState())
        private set

    fun setSelectedDate(date: String) {
<<<<<<< Updated upstream
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
=======
        // Po zmianie daty odzyskujemy zapisany stan checklisty.
        val savedExercises = _exerciseHistory[profileDateKey(date)] ?: emptySet()

        uiState = uiState.copy(
            selectedDate = date,
            checkedExercises = savedExercises
        )
    }

    fun setActiveProfile(profileId: String) {
        activeProfileId = profileId
        val date = uiState.selectedDate
        if (date.isNotBlank()) {
            setSelectedDate(date)
        }
    }

    fun toggleExercise(name: String, isChecked: Boolean) {
        val currentDate = uiState.selectedDate
        if (currentDate.isBlank()) return

        // Set upraszcza dodawanie i usuwanie zaznaczen bez duplikatow.
        val newCheckedSet = if (isChecked) {
            uiState.checkedExercises + name
        } else {
            uiState.checkedExercises - name
        }

        _exerciseHistory[profileDateKey(currentDate)] = newCheckedSet
        uiState = uiState.copy(checkedExercises = newCheckedSet)
    }

    fun resetCheckedExercises() {
        val currentDate = uiState.selectedDate
        if (currentDate.isBlank()) return

        // Reset dotyczy tylko aktualnie wybranego dnia.
        _exerciseHistory[profileDateKey(currentDate)] = emptySet()
>>>>>>> Stashed changes
        uiState = uiState.copy(checkedExercises = emptySet())
    }
}
