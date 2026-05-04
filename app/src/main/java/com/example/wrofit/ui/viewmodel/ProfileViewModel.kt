package com.example.wrofit.ui.viewmodel

<<<<<<< Updated upstream
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ProfileUiState(
    val selectedDate: String = "",
=======
import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import java.io.File
import java.util.UUID

data class ProfileUiState(
    val profileId: String = "",
    val selectedDate: String = "", // Tutaj przechowujemy date urodzenia.
    val profilePhotoUri: String = "",
>>>>>>> Stashed changes
    val fullName: String = "",
    val gender: String = "",
    val weight: String = "",
    val height: String = "",
    val goal: String = "",
    val activityLevel: String = "",
    val genderExpanded: Boolean = false,
    val activityExpanded: Boolean = false
)

<<<<<<< Updated upstream
class ProfileViewModel : ViewModel() {
    val genderOptions = listOf("Kobieta", "Mężczyzna", "Wolę nie podawać")
    val activityOptions = listOf("Wysoki", "Średni", "Niski", "Brak - siedzący")
=======
class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    // Stale listy opcji do dropdownow.
    val genderOptions = listOf("Kobieta", "Mężczyzna", "Inna", "Helikopter bojowy")
    val activityOptions = listOf("Brak (siedzący)", "Niski", "Średni", "Wysoki")
>>>>>>> Stashed changes

    var uiState by mutableStateOf(ProfileUiState())
        private set

<<<<<<< Updated upstream
=======
    private val prefs = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

    init {
        // Przy starcie odtwarzamy ostatnio zapisany profil z pamieci lokalnej telefonu.
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

    // Tutaj selectedDate oznacza date urodzenia, a nie globalna date dziennika.
>>>>>>> Stashed changes
    fun setSelectedDate(date: String) {
        uiState = uiState.copy(selectedDate = date)
    }

    fun setFullName(input: String) {
<<<<<<< Updated upstream
=======
        // Pozwalamy na litery i spacje, zeby ograniczyc przypadkowe znaki.
>>>>>>> Stashed changes
        if (input.all { it.isLetter() || it.isWhitespace() }) {
            uiState = uiState.copy(fullName = input)
        }
    }

    fun setGender(value: String) {
<<<<<<< Updated upstream
        uiState = uiState.copy(gender = value, genderExpanded = false)
=======
        uiState = uiState.copy(gender = value, genderExpanded = false, isProfileSaved = false)
    }

    private fun setProfilePhotoUri(uri: String) {
        uiState = uiState.copy(profilePhotoUri = uri, isProfileSaved = false)
>>>>>>> Stashed changes
    }

    fun saveProfilePhotoFromPickerUri(uri: Uri): Boolean {
        val application = getApplication<Application>()
        val photoDirectory = File(application.filesDir, "profile")
        val photoFile = File(photoDirectory, "profile_photo.jpg")

        return try {
            photoDirectory.mkdirs()
            application.contentResolver.openInputStream(uri)?.use { input ->
                photoFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: return false

            setProfilePhotoUri(Uri.fromFile(photoFile).toString())
            true
        } catch (_: SecurityException) {
            false
        } catch (_: java.io.IOException) {
            false
        }
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
<<<<<<< Updated upstream
        if (input.all { it.isLetter() || it.isWhitespace() }) {
            uiState = uiState.copy(goal = input)
        }
=======
        uiState = uiState.copy(goal = input, isProfileSaved = false)
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======

    fun saveProfile() {
        // Pierwszy zapis generuje identyfikator, ktory potem rozdziela dane profili na innych ekranach.
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
        // Czyscimy i dane zapisane lokalnie, i stan trzymany w ViewModelu.
        prefs.edit().clear().apply()
        uiState = ProfileUiState()
    }
>>>>>>> Stashed changes
}
