package com.example.wrofit.repository

// LiveData jest przekazywane dalej do ViewModelu i UI.
import androidx.lifecycle.LiveData
// DAO odpowiada za realną komunikację z Room.
import com.example.wrofit.data.dao.FoodDao
// Model pojedynczego wpisu jedzenia.
import com.example.wrofit.data.model.FoodEntry

// Repozytorium jest warstwą pośrednią między ViewModel a DAO.
// Dzięki temu ViewModel nie musi znać szczegółów Room.
class FoodRepository(private val dao: FoodDao) {
    // Udostępniamy listę wszystkich wpisów.
    val allEntries: LiveData<List<FoodEntry>> = dao.getAllEntries()
    // Udostępniamy łączną sumę kalorii.
    val totalCalories: LiveData<Double> = dao.getTotalCalories()

    // Ta funkcja tylko deleguje zapis do DAO.
    suspend fun insert(entry: FoodEntry) = dao.insert(entry)
    // Ta funkcja tylko deleguje usuwanie do DAO.
    suspend fun delete(entry: FoodEntry) = dao.delete(entry)
}
