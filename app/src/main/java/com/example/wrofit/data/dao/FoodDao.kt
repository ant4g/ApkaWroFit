package com.example.wrofit.data.dao

// LiveData pozwala obserwować zmiany w bazie w sposób reaktywny.
import androidx.lifecycle.LiveData
// Importujemy zestaw adnotacji Room.
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
// Import modelu reprezentującego jeden wpis jedzenia.
import com.example.wrofit.data.model.FoodEntry

// @Dao mówi Room, że ten interfejs zawiera operacje na tabeli.
@Dao
interface FoodDao {
    // @Insert generuje implementację SQL INSERT.
    // REPLACE oznacza, że przy konflikcie rekord zostanie nadpisany.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: FoodEntry)

    // @Delete generuje SQL DELETE dla przekazanego obiektu.
    @Delete
    suspend fun delete(entry: FoodEntry)

    // To zapytanie pobiera wszystkie rekordy z tabeli food_table.
    // ORDER BY id DESC sprawia, że najnowsze wpisy są na początku.
    // Zwracamy LiveData, więc UI może obserwować zmiany automatycznie.
    @Query("SELECT * FROM food_table ORDER BY id DESC")
    fun getAllEntries(): LiveData<List<FoodEntry>>

    // SUM(calories) liczy łączną liczbę kalorii ze wszystkich rekordów.
    // IFNULL zabezpiecza przed nullem, gdy tabela jest pusta.
    @Query("SELECT IFNULL(SUM(calories), 0.0) FROM food_table")
    fun getTotalCalories(): LiveData<Double>
}
