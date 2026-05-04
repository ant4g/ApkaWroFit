package com.example.wrofit.data.model

// Encja Room dla tabeli filmów instruktażowych.
import androidx.room.Entity
// Adnotacja klucza głównego.
import androidx.room.PrimaryKey

// Ta klasa opisuje pojedynczy film instruktażowy.
@Entity(tableName = "tutorial_videos")
data class TutorialVideo(
    // Tutaj ID nie jest generowane automatycznie.
    // Jest wczytywane z pliku JSON i pełni rolę stabilnego identyfikatora.
    @PrimaryKey
    val id: String,
    // Tytuł filmu pokazywany użytkownikowi.
    val title: String,
    // Nazwa pliku w folderze res/raw bez rozszerzenia.
    val resourceName: String
)
