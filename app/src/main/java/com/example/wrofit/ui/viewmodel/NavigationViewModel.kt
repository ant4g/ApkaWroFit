package com.example.wrofit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
    var selectedTab by mutableStateOf(0)
        private set

    fun updateSelectedTab(tab: Int) {
        selectedTab = tab
    }
}
