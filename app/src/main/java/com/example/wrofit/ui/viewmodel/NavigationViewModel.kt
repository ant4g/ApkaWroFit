package com.example.wrofit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
<<<<<<< Updated upstream
    var selectedTab by mutableStateOf(0)
        private set

    fun updateSelectedTab(tab: Int) {
        selectedTab = tab
    }
=======
    // Jedno zrodlo prawdy dla aktualnie wybranego ekranu.
    var selectedTab by mutableStateOf(0)
        private set

    // Jedna globalna data, wspoldzielona przez ekrany pracujace na danych dziennych.
    var selectedDate by mutableStateOf(getCurrentDate())
        private set

    fun updateSelectedTab(tab: Int) {
        selectedTab = tab
    }

    fun updateSelectedDate(date: String) {
        selectedDate = date
    }

    // Startujemy od dzisiejszej daty, aby ekran mial od razu sensowny kontekst.
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }
>>>>>>> Stashed changes
}
