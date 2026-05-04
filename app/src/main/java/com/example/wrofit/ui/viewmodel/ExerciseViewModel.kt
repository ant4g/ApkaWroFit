package com.example.wrofit.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wrofit.data.dao.ExerciseDayDao
import com.example.wrofit.data.database.WroFitDatabase
import com.example.wrofit.data.model.ExerciseDayEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

data class ExerciseUiState(
    val selectedDate: String = "",
    val checkedExercises: Set<String> = emptySet()
)

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val exerciseDayDao: ExerciseDayDao = WroFitDatabase.getDatabase(application).exerciseDayDao()

    val exercises = listOf(
        "Pajacyki", "Przysiady", "Pompki", "Deska", "Wykroki",
        "Wspinaczka", "Mostek biodrowy", "Burpees", "Rowerek", "Unoszenie nóg"
    )

    private val exerciseHistory = mutableStateMapOf<String, Set<String>>()
    private var activeProfileId: String = ""

    var uiState by mutableStateOf(ExerciseUiState())
        private set

    fun setSelectedDate(date: String) {
        val profileId = activeProfileKey()
        val memoryKey = profileDateKey(profileId, date)
        uiState = uiState.copy(selectedDate = date)
        viewModelScope.launch {
            val savedExercises = withContext(Dispatchers.IO) {
                exerciseDayDao.getDay(profileId, date)?.checkedExercisesJson?.toExerciseSet()
            } ?: exerciseHistory[memoryKey] ?: emptySet()

            if (uiState.selectedDate != date || activeProfileKey() != profileId) {
                return@launch
            }

            uiState = uiState.copy(
                selectedDate = date,
                checkedExercises = savedExercises
            )
            exerciseHistory[memoryKey] = savedExercises
        }
    }

    fun setActiveProfile(profileId: String) {
        activeProfileId = profileId
    }

    fun toggleExercise(name: String, isChecked: Boolean) {
        val currentDate = uiState.selectedDate
        if (currentDate.isBlank()) return

        val newCheckedSet = if (isChecked) {
            uiState.checkedExercises + name
        } else {
            uiState.checkedExercises - name
        }

        exerciseHistory[profileDateKey(activeProfileKey(), currentDate)] = newCheckedSet
        saveToDatabase(currentDate, newCheckedSet)
        uiState = uiState.copy(checkedExercises = newCheckedSet)
    }

    fun resetCheckedExercises() {
        val currentDate = uiState.selectedDate
        if (currentDate.isBlank()) return

        exerciseHistory[profileDateKey(activeProfileKey(), currentDate)] = emptySet()
        saveToDatabase(currentDate, emptySet())
        uiState = uiState.copy(checkedExercises = emptySet())
    }

    private fun profileDateKey(profileId: String, date: String): String = "$profileId|$date"
    private fun activeProfileKey(): String = activeProfileId.ifBlank { "guest" }

    private fun saveToDatabase(date: String, checkedExercises: Set<String>) {
        val profileId = activeProfileKey()
        val checkedExercisesJson = checkedExercises.toJson()
        viewModelScope.launch(Dispatchers.IO) {
            exerciseDayDao.upsert(
                ExerciseDayEntity(
                    profileId = profileId,
                    date = date,
                    checkedExercisesJson = checkedExercisesJson
                )
            )
        }
    }

    private fun Set<String>.toJson(): String {
        val array = JSONArray()
        forEach(array::put)
        return array.toString()
    }

    private fun String.toExerciseSet(): Set<String> {
        return runCatching {
            val array = JSONArray(this)
            List(array.length()) { index -> array.getString(index) }.toSet()
        }.getOrDefault(emptySet())
    }
}
