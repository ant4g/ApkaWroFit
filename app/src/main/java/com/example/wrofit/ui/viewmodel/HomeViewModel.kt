package com.example.wrofit.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.wrofit.data.database.WroFitDatabase
import com.example.wrofit.repository.GalleryRepository
import com.example.wrofit.repository.TutorialVideoRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GalleryRepository
    private val tutorialVideoRepository: TutorialVideoRepository
    val galleryImages
        get() = repository.allImages
    val tutorialVideo
        get() = tutorialVideoRepository.getFirstVideo()

    var isTutorialVideoVisible by mutableStateOf(false)
        private set
    var isPositionsGalleryVisible by mutableStateOf(false)
        private set

    init {
        val database = WroFitDatabase.getDatabase(application)
        repository = GalleryRepository(database.galleryImageDao())
        tutorialVideoRepository = TutorialVideoRepository(database.tutorialVideoDao())
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
