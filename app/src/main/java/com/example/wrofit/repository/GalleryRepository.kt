package com.example.wrofit.repository

// LiveData pozwala przekazać dane galerii do ViewModelu i Compose.
import androidx.lifecycle.LiveData
// DAO tabeli galerii.
import com.example.wrofit.data.dao.GalleryImageDao
// Model jednego obrazka.
import com.example.wrofit.data.model.GalleryImage

// Repozytorium galerii izoluje ViewModel od szczegółów bazy.
class GalleryRepository(private val dao: GalleryImageDao) {
    // allImages to reaktywny strumień wszystkich obrazków.
    val allImages: LiveData<List<GalleryImage>> = dao.getAllImages()

    // insertAll przydaje się przy seedowaniu lub odświeżaniu konfiguracji.
    suspend fun insertAll(items: List<GalleryImage>) = dao.insertAll(items)
    // count() pozwala sprawdzić, czy tabela jest już zapełniona.
    suspend fun count(): Int = dao.count()
}
