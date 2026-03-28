package com.example.wrofit.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import java.util.UUID

data class ProfileUiState(
    val profileId: String = "",
    val selectedDate: String = "", // Tutaj przechowujemy datę urodzenia
    val profilePhotoUri: String = "",
    val fullName: String = "",
    val gender: String = "",
    val weight: String = "",
    val height: String = "",
    val goal: String = "",
    val activityLevel: String = "",
    val isProfileSaved: Boolean = false,
    val genderExpanded: Boolean = false,
    val activityExpanded: Boolean = false
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    // Opcje zgodne z dokumentacją projektu WroFit
    val genderOptions = listOf("Kobieta", "Mężczyzna", "Inna", "Helikopter bojowy")
    val activityOptions = listOf("Brak (siedzący)", "Niski", "Średni", "Wysoki")

    var uiState by mutableStateOf(ProfileUiState())
        private set

    private val prefs = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

    init {
        uiState = uiState.copy(
            profileId = prefs.getString("profileId", "") ?: "",
            selectedDate = prefs.getString("selectedDate", "") ?: "",
            profilePhotoUri = prefs.getString("profilePhotoUri", "") ?: "",
            fullName = prefs.getString("fullName", "") ?: "",
            gender = prefs.getString("gender", "") ?: "",
            weight = prefs.getString("weight", "") ?: "",
            height = prefs.getString("height", "") ?: "",
            goal = prefs.getString("goal", "") ?: "",
            activityLevel = prefs.getString("activityLevel", "") ?: "",
            isProfileSaved = prefs.getBoolean("isProfileSaved", false)
        )
    }

    // Ustawia datę urodzenia wybraną z kalendarza
    fun setSelectedDate(date: String) {
        uiState = uiState.copy(selectedDate = date, isProfileSaved = false)
    }

    fun setFullName(input: String) {
        // Pozwalamy na litery, spacje i polskie znaki
        if (input.all { it.isLetter() || it.isWhitespace() }) {
            uiState = uiState.copy(fullName = input, isProfileSaved = false)
        }
    }

    fun setGender(value: String) {
        uiState = uiState.copy(gender = value, genderExpanded = false, isProfileSaved = false)
    }

    fun setProfilePhotoUri(uri: String) {
        uiState = uiState.copy(profilePhotoUri = uri, isProfileSaved = false)
    }

    fun setWeight(input: String) {
        // Tylko cyfry dla wagi
        if (input.all { it.isDigit() }) {
            uiState = uiState.copy(weight = input, isProfileSaved = false)
        }
    }

    fun setHeight(input: String) {
        // Tylko cyfry dla wzrostu
        if (input.all { it.isDigit() }) {
            uiState = uiState.copy(height = input, isProfileSaved = false)
        }
    }

    fun setGoal(input: String) {
        // Dowolny tekst dla celu
        uiState = uiState.copy(goal = input, isProfileSaved = false)
    }

    fun setActivityLevel(value: String) {
        uiState = uiState.copy(activityLevel = value, activityExpanded = false, isProfileSaved = false)
    }

    fun setGenderExpanded(expanded: Boolean) {
        uiState = uiState.copy(genderExpanded = expanded)
    }

    fun setActivityExpanded(expanded: Boolean) {
        uiState = uiState.copy(activityExpanded = expanded)
    }

    fun saveProfile() {
        val savedProfileId = uiState.profileId.ifBlank { UUID.randomUUID().toString() }
        uiState = uiState.copy(profileId = savedProfileId, isProfileSaved = true)
        prefs.edit()
            .putString("profileId", uiState.profileId)
            .putString("selectedDate", uiState.selectedDate)
            .putString("profilePhotoUri", uiState.profilePhotoUri)
            .putString("fullName", uiState.fullName)
            .putString("gender", uiState.gender)
            .putString("weight", uiState.weight)
            .putString("height", uiState.height)
            .putString("goal", uiState.goal)
            .putString("activityLevel", uiState.activityLevel)
            .putBoolean("isProfileSaved", true)
            .apply()
    }

    fun editProfile() {
        uiState = uiState.copy(isProfileSaved = false)
    }

    fun deleteProfile() {
        prefs.edit().clear().apply()
        uiState = ProfileUiState()
    }
}
