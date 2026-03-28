package com.example.wrofit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NavigationViewModel : ViewModel() {
    // Zarządzanie wybraną zakładką menu dolnego
    var selectedTab by mutableStateOf(0)
        private set

    // Globalna data dla całej aplikacji - zapewnia, że zmiana daty na jednym ekranie
    // jest widoczna na pozostałych (Food, Sleep, Exercise)
    var selectedDate by mutableStateOf(getCurrentDate())
        private set

    fun updateSelectedTab(tab: Int) {
        selectedTab = tab
    }

    fun updateSelectedDate(date: String) {
        selectedDate = date
    }

    // Pomocnicza funkcja do ustawienia dzisiejszej daty na start
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }
}