package com.example.wrofit.data.model

// Room będzie mapował tę klasę na tabelę.
import androidx.room.Entity
// PrimaryKey oznacza kolumnę identyfikującą wiersz.
import androidx.room.PrimaryKey

// Ta encja reprezentuje jeden obrazek w galerii pozycji.
@Entity(tableName = "gallery_images")
data class GalleryImage(
    // ID jest generowane automatycznie przez Room.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Tytuł widoczny w UI, np. opis pozycji ćwiczenia.
    val title: String,
    // Nazwa pliku drawable bez rozszerzenia.
    // Potem UI zamienia ten tekst na prawdziwy zasób Androida.
    val drawableName: String
)
