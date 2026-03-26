package com.example.wrofit.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var isTutorialVideoVisible by mutableStateOf(false)
        private set
    var isPositionsGalleryVisible by mutableStateOf(false)
        private set

    fun showTutorialVideo() {
        isTutorialVideoVisible = true
    }

    fun hideTutorialVideo() {
        isTutorialVideoVisible = false
    }

    fun showPositionsGallery() {
        isPositionsGalleryVisible = true
    }

    fun hidePositionsGallery() {
        isPositionsGalleryVisible = false
    }
}
