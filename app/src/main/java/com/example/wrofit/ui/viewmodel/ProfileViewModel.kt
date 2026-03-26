package com.example.wrofit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ProfileUiState(
    val selectedDate: String = "",
    val fullName: String = "",
    val gender: String = "",
    val weight: String = "",
    val height: String = "",
    val goal: String = "",
    val activityLevel: String = "",
    val genderExpanded: Boolean = false,
    val activityExpanded: Boolean = false
)

class ProfileViewModel : ViewModel() {
    val genderOptions = listOf("Kobieta", "Mężczyzna", "Wolę nie podawać")
    val activityOptions = listOf("Wysoki", "Średni", "Niski", "Brak - siedzący")

    var uiState by mutableStateOf(ProfileUiState())
        private set

    fun setSelectedDate(date: String) {
        uiState = uiState.copy(selectedDate = date)
    }

    fun setFullName(input: String) {
        if (input.all { it.isLetter() || it.isWhitespace() }) {
            uiState = uiState.copy(fullName = input)
        }
    }

    fun setGender(value: String) {
        uiState = uiState.copy(gender = value, genderExpanded = false)
    }

    fun setWeight(input: String) {
        if (input.all { it.isDigit() }) {
            uiState = uiState.copy(weight = input)
        }
    }

    fun setHeight(input: String) {
        if (input.all { it.isDigit() }) {
            uiState = uiState.copy(height = input)
        }
    }

    fun setGoal(input: String) {
        if (input.all { it.isLetter() || it.isWhitespace() }) {
            uiState = uiState.copy(goal = input)
        }
    }

    fun setActivityLevel(value: String) {
        uiState = uiState.copy(activityLevel = value, activityExpanded = false)
    }

    fun setGenderExpanded(expanded: Boolean) {
        uiState = uiState.copy(genderExpanded = expanded)
    }

    fun setActivityExpanded(expanded: Boolean) {
        uiState = uiState.copy(activityExpanded = expanded)
    }
}
