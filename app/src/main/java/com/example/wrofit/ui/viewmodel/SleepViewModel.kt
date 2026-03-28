package com.example.wrofit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// Pomocnicza klasa do przechowywania danych snu dla konkretnego dnia
data class SleepDayData(
    val sleepTime: String = "22:00",
    val wakeTime: String = "07:00",
    val difficulties: String = "",
    val sleepGoalHours: String = "8"
)

data class SleepUiState(
    val selectedDate: String = "",
    val sleepTime: String = "22:00",
    val wakeTime: String = "07:00",
    val difficulties: String = "",
    val sleepGoalHours: String = "8"
)

class SleepViewModel : ViewModel() {
    // Mapa przechowująca historię snu: Data -> Dane o śnie
    private val _sleepHistory = mutableStateMapOf<String, SleepDayData>()
    private var activeProfileId: String = ""

    var uiState by mutableStateOf(SleepUiState())
        private set

    fun setSelectedDate(date: String) {
        // Przy zmianie daty pobierz dane z mapy lub ustaw domyślne, jeśli to nowy dzień
        val dayData = _sleepHistory[profileDateKey(date)] ?: SleepDayData()

        uiState = uiState.copy(
            selectedDate = date,
            sleepTime = dayData.sleepTime,
            wakeTime = dayData.wakeTime,
            difficulties = dayData.difficulties,
            sleepGoalHours = dayData.sleepGoalHours
        )
    }

    fun setActiveProfile(profileId: String) {
        activeProfileId = profileId
        val date = uiState.selectedDate
        if (date.isNotBlank()) {
            setSelectedDate(date)
        }
    }

    fun setSleepTime(time: String) {
        uiState = uiState.copy(sleepTime = time)
        saveToMap()
    }

    fun setWakeTime(time: String) {
        uiState = uiState.copy(wakeTime = time)
        saveToMap()
    }

    fun setDifficulties(value: String) {
        uiState = uiState.copy(difficulties = value)
        saveToMap()
    }

    fun setSleepGoalHours(value: String) {
        if (value.all(Char::isDigit)) {
            uiState = uiState.copy(sleepGoalHours = value)
            saveToMap()
        }
    }

    // Funkcja zapisująca aktualny stan UI do mapy pod wybraną datą
    private fun saveToMap() {
        val date = uiState.selectedDate
        if (date.isNotBlank()) {
            _sleepHistory[profileDateKey(date)] = SleepDayData(
                sleepTime = uiState.sleepTime,
                wakeTime = uiState.wakeTime,
                difficulties = uiState.difficulties,
                sleepGoalHours = uiState.sleepGoalHours
            )
        }
    }

    private fun profileDateKey(date: String): String = "${activeProfileId.ifBlank { "guest" }}|$date"

    fun sleepGoal(): Int = uiState.sleepGoalHours.toIntOrNull() ?: 0

    fun sleptHours(): Double {
        val sleepMinutes = timeToMinutes(uiState.sleepTime)
        val wakeMinutes = timeToMinutes(uiState.wakeTime)
        val totalMinutes = if (wakeMinutes >= sleepMinutes) {
            wakeMinutes - sleepMinutes
        } else {
            24 * 60 - sleepMinutes + wakeMinutes
        }
        return totalMinutes / 60.0
    }

    private fun timeToMinutes(value: String): Int {
        val hour = value.substringBefore(":").toIntOrNull() ?: 0
        val minute = value.substringAfter(":", "0").toIntOrNull() ?: 0
        return hour * 60 + minute
    }
}
