package com.example.wrofit.data.dao

// LiveData pozwala widokowi śledzić zmiany w rekordzie filmu.
import androidx.lifecycle.LiveData
// Importy adnotacji Room.
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
// Model filmu instruktażowego.
import com.example.wrofit.data.model.TutorialVideo

// DAO dla tabeli tutorial_videos.
@Dao
interface TutorialVideoDao {
    // Wstawienie listy filmów, np. podczas seedowania z pliku JSON.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TutorialVideo>)

    // Pobieramy pierwszy film z tabeli.
    // LIMIT 1 oznacza, że interesuje nas tylko jeden rekord.
    // Projekt aktualnie zakłada jeden główny film instruktażowy.
    @Query("SELECT * FROM tutorial_videos ORDER BY id ASC LIMIT 1")
    fun getFirstVideo(): LiveData<TutorialVideo?>

    // Liczba rekordów w tabeli służy do sprawdzenia, czy dane już istnieją.
    @Query("SELECT COUNT(*) FROM tutorial_videos")
    suspend fun count(): Int
}
