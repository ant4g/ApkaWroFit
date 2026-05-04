package com.example.wrofit.repository

// LiveData dla reaktywnej obserwacji filmu.
import androidx.lifecycle.LiveData
// DAO filmów instruktażowych.
import com.example.wrofit.data.dao.TutorialVideoDao
// Model filmu.
import com.example.wrofit.data.model.TutorialVideo

// Repozytorium dla tabeli tutorial_videos.
class TutorialVideoRepository(private val dao: TutorialVideoDao) {
    // Zwracamy pierwszy film jako LiveData, aby UI mogło go obserwować.
    fun getFirstVideo(): LiveData<TutorialVideo?> = dao.getFirstVideo()

    // Delegacja zapisu wielu rekordów do DAO.
    suspend fun insertAll(items: List<TutorialVideo>) = dao.insertAll(items)
    // Delegacja liczenia rekordów do DAO.
    suspend fun count(): Int = dao.count()
}
