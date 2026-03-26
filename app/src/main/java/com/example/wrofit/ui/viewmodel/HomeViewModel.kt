package com.example.wrofit.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.wrofit.data.database.WroFitDatabase
import com.example.wrofit.repository.GalleryRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GalleryRepository
    val galleryImages
        get() = repository.allImages

    var isTutorialVideoVisible by mutableStateOf(false)
        private set
    var isPositionsGalleryVisible by mutableStateOf(false)
        private set

    init {
        val dao = WroFitDatabase.getDatabase(application).galleryImageDao()
        repository = GalleryRepository(dao)
    }

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
