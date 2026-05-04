package com.example.wrofit.data.model

// Import adnotacji Entity, która mówi Room, że ta klasa ma odpowiadać tabeli w bazie.
import androidx.room.Entity
// Import adnotacji PrimaryKey, która oznacza pole klucza głównego.
import androidx.room.PrimaryKey

// Deklarujemy encję Room.
// Room na podstawie tej klasy utworzy tabelę o nazwie "food_table".
@Entity(tableName = "food_table")
// data class to wygodny typ danych w Kotlinie.
// Przechowuje pojedynczy wpis jedzenia.
data class FoodEntry(
    // To pole jest kluczem głównym tabeli.
    // autoGenerate = true oznacza, że Room sam nada kolejne ID.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Nazwa posiłku, np. "Owsianka".
    val mealName: String,
    // Liczba kalorii dla tego wpisu.
    val calories: Double,
    // Typ posiłku, np. "Śniadanie", "Obiad", "Kolacja".
    val mealType: String,
    // Data, do której przypisany jest wpis.
    // W tym projekcie data jest przechowywana jako String.
    val date: String
)
