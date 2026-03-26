package com.example.wrofit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class SleepUiState(
    val selectedDate: String = "",
    val sleepTime: String = "",
    val wakeTime: String = "",
    val difficulties: String = ""
)

class SleepViewModel : ViewModel() {
    var uiState by mutableStateOf(SleepUiState())
        private set

    fun setSelectedDate(date: String) {
        uiState = uiState.copy(selectedDate = date)
    }

    fun setSleepTime(time: String) {
        uiState = uiState.copy(sleepTime = time)
    }

    fun setWakeTime(time: String) {
        uiState = uiState.copy(wakeTime = time)
    }

    fun setDifficulties(value: String) {
        uiState = uiState.copy(difficulties = value)
    }
}
