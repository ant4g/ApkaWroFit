package com.example.wrofit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

<<<<<<< Updated upstream
=======
// Snapshot danych snu dla jednego dnia i jednego profilu.
data class SleepDayData(
    val sleepTime: String = "22:00",
    val wakeTime: String = "07:00",
    val difficulties: String = "",
    val sleepGoalHours: String = "8"
)

>>>>>>> Stashed changes
data class SleepUiState(
    val selectedDate: String = "",
    val sleepTime: String = "",
    val wakeTime: String = "",
    val difficulties: String = ""
)

class SleepViewModel : ViewModel() {
<<<<<<< Updated upstream
=======
    // Dane sa grupowane po kluczu "profil|data", wiec profile nie nadpisuja sie nawzajem.
    private val _sleepHistory = mutableStateMapOf<String, SleepDayData>()
    private var activeProfileId: String = ""

>>>>>>> Stashed changes
    var uiState by mutableStateOf(SleepUiState())
        private set

    fun setSelectedDate(date: String) {
<<<<<<< Updated upstream
        uiState = uiState.copy(selectedDate = date)
=======
        // Zmiana dnia oznacza podmiane calego stanu formularza na zapis z mapy.
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
>>>>>>> Stashed changes
    }

    fun setSleepTime(time: String) {
        uiState = uiState.copy(sleepTime = time)
    }

    fun setWakeTime(time: String) {
        uiState = uiState.copy(wakeTime = time)
    }

    fun setDifficulties(value: String) {
        uiState = uiState.copy(difficulties = value)
<<<<<<< Updated upstream
=======
        saveToMap()
    }

    fun setSleepGoalHours(value: String) {
        if (value.all(Char::isDigit)) {
            uiState = uiState.copy(sleepGoalHours = value)
            saveToMap()
        }
    }

    private fun saveToMap() {
        val date = uiState.selectedDate
        if (date.isNotBlank()) {
            // Przechowujemy kopie calego formularza, aby latwo odtworzyc widok po powrocie do daty.
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
        // Gdy pobudka wypada "wczesniej" niz sen, interpretujemy to jako przejscie przez polnoc.
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
>>>>>>> Stashed changes
    }
}
