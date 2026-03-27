package com.example.wrofit.repository

import androidx.lifecycle.LiveData
import com.example.wrofit.data.dao.TutorialVideoDao
import com.example.wrofit.data.model.TutorialVideo

class TutorialVideoRepository(private val dao: TutorialVideoDao) {
    fun getFirstVideo(): LiveData<TutorialVideo?> = dao.getFirstVideo()

    suspend fun insertAll(items: List<TutorialVideo>) = dao.insertAll(items)
    suspend fun count(): Int = dao.count()
}
