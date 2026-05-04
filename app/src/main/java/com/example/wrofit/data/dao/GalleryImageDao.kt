package com.example.wrofit.data.dao

// LiveData do reaktywnej obserwacji tabeli galerii.
import androidx.lifecycle.LiveData
// Podstawowe adnotacje Room dla DAO.
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
// Model jednego obrazka w galerii.
import com.example.wrofit.data.model.GalleryImage

// DAO obsługujące tabelę gallery_images.
@Dao
interface GalleryImageDao {
    // insertAll zapisuje listę obiektów naraz.
    // REPLACE ułatwia seedowanie danych z pliku JSON.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GalleryImage>)

    // Pobieramy wszystkie obrazki posortowane po rosnącym ID.
    // Dzięki LiveData ekran galerii zaktualizuje się automatycznie.
    @Query("SELECT * FROM gallery_images ORDER BY id ASC")
    fun getAllImages(): LiveData<List<GalleryImage>>

    // count() służy głównie do sprawdzenia, czy tabela jest pusta.
    // To jest używane przy seedowaniu bazy.
    @Query("SELECT COUNT(*) FROM gallery_images")
    suspend fun count(): Int
}
