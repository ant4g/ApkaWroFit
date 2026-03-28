package com.example.wrofit.repository

import androidx.lifecycle.LiveData
import com.example.wrofit.data.dao.GalleryImageDao
import com.example.wrofit.data.model.GalleryImage

class GalleryRepository(private val dao: GalleryImageDao) {
    val allImages: LiveData<List<GalleryImage>> = dao.getAllImages()

    suspend fun insertAll(items: List<GalleryImage>) = dao.insertAll(items)
    suspend fun count(): Int = dao.count()
}
