package com.example.wrofit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ExerciseUiState(
    val selectedDate: String = "",
    val checkedExercises: Set<String> = emptySet()
)

class ExerciseViewModel : ViewModel() {
    // Lista ćwiczeń zgodna z dokumentacją WroFit
    val exercises = listOf(
        "Pajacyki", "Przysiady", "Pompki", "Deska", "Wykroki",
        "Wspinaczka", "Mostek biodrowy", "Burpees", "Rowerek", "Unoszenie nóg"
    )

    // Mapa przechowująca stan dla każdej daty: Klucz to String (data), wartość to Set (zbiór ćwiczeń)
    private val _exerciseHistory = mutableStateMapOf<String, Set<String>>()
    private var activeProfileId: String = ""

    var uiState by mutableStateOf(ExerciseUiState())
        private set

    fun setSelectedDate(date: String) {
        // 1. Pobierz zapisane ćwiczenia dla nowej daty (jeśli brak, zwróć pusty zbiór)
        val savedExercises = _exerciseHistory[profileDateKey(date)] ?: emptySet()

        // 2. Aktualizuj uiState nową datą i przypisanymi do niej danymi
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

        // Oblicz nowy zbiór zaznaczeń
        val newCheckedSet = if (isChecked) {
            uiState.checkedExercises + name
        } else {
            uiState.checkedExercises - name
        }

        // 1. Zaktualizuj mapę (pamięć trwałą wewnątrz ViewModelu)
        _exerciseHistory[profileDateKey(currentDate)] = newCheckedSet

        // 2. Zaktualizuj UI
        uiState = uiState.copy(checkedExercises = newCheckedSet)
    }

    fun resetCheckedExercises() {
        val currentDate = uiState.selectedDate
        if (currentDate.isBlank()) return

        // Wyczyść dane dla bieżącej daty w mapie i w UI
        _exerciseHistory[profileDateKey(currentDate)] = emptySet()
        uiState = uiState.copy(checkedExercises = emptySet())
    }

    private fun profileDateKey(date: String): String = "${activeProfileId.ifBlank { "guest" }}|$date"
}
