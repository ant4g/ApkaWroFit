package com.example.wrofit.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wrofit.data.dao.SleepDayDao
import com.example.wrofit.data.database.WroFitDatabase
import com.example.wrofit.data.model.SleepDayEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

class SleepViewModel(application: Application) : AndroidViewModel(application) {
    private val sleepDayDao: SleepDayDao = WroFitDatabase.getDatabase(application).sleepDayDao()

    // Mapa przechowująca historię snu: Data -> Dane o śnie
    private val _sleepHistory = mutableStateMapOf<String, SleepDayData>()
    private var activeProfileId: String = ""

    var uiState by mutableStateOf(SleepUiState())
        private set

    fun setSelectedDate(date: String) {
        // Przy zmianie daty pobierz dane z mapy lub ustaw domyślne, jeśli to nowy dzień
        val profileId = activeProfileKey()
        val memoryKey = profileDateKey(profileId, date)
        uiState = uiState.copy(selectedDate = date)
        viewModelScope.launch {
            val dayData = withContext(Dispatchers.IO) {
                sleepDayDao.getDay(profileId, date)?.toDayData()
            } ?: _sleepHistory[memoryKey] ?: SleepDayData()

            if (uiState.selectedDate != date || activeProfileKey() != profileId) {
                return@launch
            }

            uiState = uiState.copy(
                selectedDate = date,
                sleepTime = dayData.sleepTime,
                wakeTime = dayData.wakeTime,
                difficulties = dayData.difficulties,
                sleepGoalHours = dayData.sleepGoalHours
            )
            _sleepHistory[memoryKey] = dayData
        }
    }

    fun setActiveProfile(profileId: String) {
        activeProfileId = profileId
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
        val profileId = activeProfileKey()
        if (date.isNotBlank()) {
            val dayData = SleepDayData(
                sleepTime = uiState.sleepTime,
                wakeTime = uiState.wakeTime,
                difficulties = uiState.difficulties,
                sleepGoalHours = uiState.sleepGoalHours
            )
            _sleepHistory[profileDateKey(profileId, date)] = dayData
            viewModelScope.launch(Dispatchers.IO) {
                sleepDayDao.upsert(dayData.toEntity(profileId, date))
            }
        }
    }

    private fun profileDateKey(profileId: String, date: String): String = "$profileId|$date"
    private fun activeProfileKey(): String = activeProfileId.ifBlank { "guest" }

    private fun SleepDayData.toEntity(profileId: String, date: String): SleepDayEntity {
        return SleepDayEntity(
            profileId = profileId,
            date = date,
            sleepTime = sleepTime,
            wakeTime = wakeTime,
            difficulties = difficulties,
            sleepGoalHours = sleepGoalHours
        )
    }

    private fun SleepDayEntity.toDayData(): SleepDayData {
        return SleepDayData(
            sleepTime = sleepTime,
            wakeTime = wakeTime,
            difficulties = difficulties,
            sleepGoalHours = sleepGoalHours
        )
    }

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
