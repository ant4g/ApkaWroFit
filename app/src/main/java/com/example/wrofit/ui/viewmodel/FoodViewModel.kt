package com.example.wrofit.ui.viewmodel

// Application jest potrzebny, bo ten ViewModel dziedziczy po AndroidViewModel.
import android.app.Application
// MutableState to opakowanie stanu obserwowanego przez Compose.
import androidx.compose.runtime.MutableState
<<<<<<< Updated upstream
=======
// mutableStateMapOf tworzy reaktywną mapę obserwowaną przez Compose.
import androidx.compose.runtime.mutableStateMapOf
// mutableStateOf tworzy pojedynczą reaktywną wartość.
>>>>>>> Stashed changes
import androidx.compose.runtime.mutableStateOf
// AndroidViewModel daje dostęp do Application.
import androidx.lifecycle.AndroidViewModel
// LiveData jest używane w kontakcie z Room.
import androidx.lifecycle.LiveData
// viewModelScope tworzy coroutine powiązane z życiem ViewModelu.
import androidx.lifecycle.viewModelScope
// Import bazy danych.
import com.example.wrofit.data.database.WroFitDatabase
// Import modelu encji jedzenia.
import com.example.wrofit.data.model.FoodEntry
// Import repozytorium warstwy jedzenia.
import com.example.wrofit.repository.FoodRepository
// Dispatchers.IO to wątek do operacji wejścia/wyjścia.
import kotlinx.coroutines.Dispatchers
// launch uruchamia coroutine.
import kotlinx.coroutines.launch

<<<<<<< Updated upstream
=======
// Ta klasa opisuje jeden wiersz formularza posiłku na ekranie.
data class MealItemData(
    // name to nazwa dania wpisana przez użytkownika.
    val name: String = "",
    // calories to wartość kalorii, trzymana jako tekst z pola formularza.
    val calories: String = ""
)

// Snapshot całego dnia żywieniowego dla jednego dnia i jednego profilu.
data class FoodDayData(
    // Lista trzech pozycji śniadania.
    val breakfast: List<MealItemData> = List(3) { MealItemData() },
    // Lista trzech pozycji obiadu.
    val lunch: List<MealItemData> = List(3) { MealItemData() },
    // Lista trzech pozycji kolacji.
    val dinner: List<MealItemData> = List(3) { MealItemData() },
    // Dzienny limit kalorii jako tekst.
    val dailyCalorieGoal: String = "2000"
)

// To jest stan czytany bezpośrednio przez ekran Compose.
>>>>>>> Stashed changes
data class FoodUiState(
    // Aktualnie wybrana data.
    val selectedDate: String = "",
    // Czy sekcja śniadania jest rozwinięta.
    val breakfastExpanded: Boolean = true,
    // Czy sekcja obiadu jest rozwinięta.
    val lunchExpanded: Boolean = false,
    // Czy sekcja kolacji jest rozwinięta.
    val dinnerExpanded: Boolean = false,
<<<<<<< Updated upstream
    val breakfastCalories: List<String> = listOf("", "", ""),
    val lunchCalories: List<String> = listOf("", "", ""),
    val dinnerCalories: List<String> = listOf("", "", "")
=======
    // Cel kalorii w formie tekstowej.
    val dailyCalorieGoal: String = "2000",
    // Lista pól śniadania.
    val breakfastItems: List<MealItemData> = List(3) { MealItemData() },
    // Lista pól obiadu.
    val lunchItems: List<MealItemData> = List(3) { MealItemData() },
    // Lista pól kolacji.
    val dinnerItems: List<MealItemData> = List(3) { MealItemData() }
>>>>>>> Stashed changes
)

// ViewModel trzyma logikę i stan ekranu Food.
class FoodViewModel(application: Application) : AndroidViewModel(application) {

    // Repozytorium pośredniczy między ViewModel a DAO.
    private val repository: FoodRepository
    // allEntries udostępnia wszystkie wpisy z Room.
    val allEntries: LiveData<List<FoodEntry>>
    // totalCalories udostępnia sumę kalorii obliczoną w bazie.
    val totalCalories: LiveData<Double>
<<<<<<< Updated upstream
=======
    // activeProfileId mówi, dla którego profilu działamy.
    private var activeProfileId: String = ""

    // Mapa "profil|data" -> dane dnia.
    // To tutaj ekran pamięta wpisy podczas przechodzenia między datami.
    private val _dailyFoodData = mutableStateMapOf<String, FoodDayData>()

    // Wewnętrzny reaktywny stan UI.
>>>>>>> Stashed changes
    private val uiStateState: MutableState<FoodUiState> = mutableStateOf(FoodUiState())
    // Publiczny getter udostępnia tylko odczyt stanu.
    val uiState: FoodUiState
        get() = uiStateState.value

    // init wykonuje się przy utworzeniu ViewModelu.
    init {
        // Pobieramy DAO z singletona bazy.
        val dao = WroFitDatabase.getDatabase(application).foodDao()
        // Tworzymy repozytorium na bazie DAO.
        repository = FoodRepository(dao)
        // Przekazujemy LiveData z repozytorium dalej.
        allEntries = repository.allEntries
        totalCalories = repository.totalCalories
    }

<<<<<<< Updated upstream
    fun insert(entry: FoodEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entry)
    }

    fun delete(entry: FoodEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(entry)
    }

    fun setSelectedDate(date: String) {
        uiStateState.value = uiState.copy(selectedDate = date)
    }

=======
    // Funkcja reaguje na zmianę daty.
    fun setSelectedDate(date: String) {
        // Pobieramy zapisany dzień dla konkretnego profilu i daty.
        // Jeśli nic nie ma, tworzymy pusty domyślny dzień.
        val dayData = _dailyFoodData[profileDateKey(date)] ?: FoodDayData()

        // Podmieniamy cały stan ekranu na dane przypisane do tej daty.
        uiStateState.value = uiState.copy(
            selectedDate = date,
            dailyCalorieGoal = dayData.dailyCalorieGoal,
            breakfastItems = dayData.breakfast,
            lunchItems = dayData.lunch,
            dinnerItems = dayData.dinner
        )
    }

    // Aktualizacja celu kalorii.
    fun setDailyCalorieGoal(value: String) {
        // Akceptujemy tylko cyfry.
        if (value.all(Char::isDigit)) {
            // Aktualizujemy stan Compose.
            uiStateState.value = uiState.copy(dailyCalorieGoal = value)
            // Zapisujemy zmiany do mapy dnia.
            saveToMap()
        }
    }

    // Ustawienie aktywnego profilu.
    fun setActiveProfile(profileId: String) {
        // Zapamiętujemy nowy profil.
        activeProfileId = profileId
        // Pobieramy aktualnie ustawioną datę z UI.
        val date = uiState.selectedDate
        // Jeśli data już istnieje, trzeba przeładować stan dla nowego profilu.
        if (date.isNotBlank()) {
            setSelectedDate(date)
        }
    }

    // Aktualizacja nazwy śniadania na danym indeksie.
    fun setBreakfastName(index: Int, value: String) {
        // Filtrujemy tylko litery i spacje.
        val sanitizedValue = value.filter { it.isLetter() || it.isWhitespace() }
        // Tworzymy nową listę z podmienionym elementem.
        val updatedList = uiState.breakfastItems.updatedAt(index) { it.copy(name = sanitizedValue) }
        // Podmieniamy stan UI.
        uiStateState.value = uiState.copy(breakfastItems = updatedList)
        // Zapisujemy do mapy.
        saveToMap()
    }

    // Aktualizacja kalorii śniadania.
    fun setBreakfastCalories(index: Int, value: String) {
        val updatedList = uiState.breakfastItems.updatedAt(index) { it.copy(calories = value) }
        uiStateState.value = uiState.copy(breakfastItems = updatedList)
        saveToMap()
    }

    // Aktualizacja nazwy obiadu.
    fun setLunchName(index: Int, value: String) {
        val sanitizedValue = value.filter { it.isLetter() || it.isWhitespace() }
        val updatedList = uiState.lunchItems.updatedAt(index) { it.copy(name = sanitizedValue) }
        uiStateState.value = uiState.copy(lunchItems = updatedList)
        saveToMap()
    }

    // Aktualizacja kalorii obiadu.
    fun setLunchCalories(index: Int, value: String) {
        val updatedList = uiState.lunchItems.updatedAt(index) { it.copy(calories = value) }
        uiStateState.value = uiState.copy(lunchItems = updatedList)
        saveToMap()
    }

    // Aktualizacja nazwy kolacji.
    fun setDinnerName(index: Int, value: String) {
        val sanitizedValue = value.filter { it.isLetter() || it.isWhitespace() }
        val updatedList = uiState.dinnerItems.updatedAt(index) { it.copy(name = sanitizedValue) }
        uiStateState.value = uiState.copy(dinnerItems = updatedList)
        saveToMap()
    }

    // Aktualizacja kalorii kolacji.
    fun setDinnerCalories(index: Int, value: String) {
        val updatedList = uiState.dinnerItems.updatedAt(index) { it.copy(calories = value) }
        uiStateState.value = uiState.copy(dinnerItems = updatedList)
        saveToMap()
    }

    // Zapis bieżącego widoku do mapy pamięci.
    private fun saveToMap() {
        // Pobieramy aktualną datę z uiState.
        val date = uiState.selectedDate
        // Jeśli data jest ustawiona, zapisujemy stan.
        if (date.isNotBlank()) {
            _dailyFoodData[profileDateKey(date)] = FoodDayData(
                breakfast = uiState.breakfastItems,
                lunch = uiState.lunchItems,
                dinner = uiState.dinnerItems,
                dailyCalorieGoal = uiState.dailyCalorieGoal
            )
        }
    }

    // Budujemy klucz z profilu i daty, aby dane różnych użytkowników się nie mieszały.
    private fun profileDateKey(date: String): String = "${activeProfileId.ifBlank { "guest" }}|$date"

    // Przełączenie rozwinięcia sekcji śniadania.
>>>>>>> Stashed changes
    fun toggleBreakfast() {
        uiStateState.value = uiState.copy(breakfastExpanded = !uiState.breakfastExpanded)
    }

    // Przełączenie rozwinięcia sekcji obiadu.
    fun toggleLunch() {
        uiStateState.value = uiState.copy(lunchExpanded = !uiState.lunchExpanded)
    }

    // Przełączenie rozwinięcia sekcji kolacji.
    fun toggleDinner() {
        uiStateState.value = uiState.copy(dinnerExpanded = !uiState.dinnerExpanded)
    }

<<<<<<< Updated upstream
    fun setBreakfastCalories(index: Int, value: String) {
        uiStateState.value = uiState.copy(breakfastCalories = uiState.breakfastCalories.updatedAt(index, value))
    }

    fun setLunchCalories(index: Int, value: String) {
        uiStateState.value = uiState.copy(lunchCalories = uiState.lunchCalories.updatedAt(index, value))
    }

    fun setDinnerCalories(index: Int, value: String) {
        uiStateState.value = uiState.copy(dinnerCalories = uiState.dinnerCalories.updatedAt(index, value))
    }

    fun breakfastTotal(): Int = caloriesTotal(uiState.breakfastCalories)

    fun lunchTotal(): Int = caloriesTotal(uiState.lunchCalories)

    fun dinnerTotal(): Int = caloriesTotal(uiState.dinnerCalories)

    fun overallTotal(): Int = breakfastTotal() + lunchTotal() + dinnerTotal()

    private fun List<String>.updatedAt(index: Int, value: String): List<String> {
=======
    // Suma kalorii śniadania.
    fun breakfastTotal(): Int = caloriesTotal(uiState.breakfastItems)
    // Suma kalorii obiadu.
    fun lunchTotal(): Int = caloriesTotal(uiState.lunchItems)
    // Suma kalorii kolacji.
    fun dinnerTotal(): Int = caloriesTotal(uiState.dinnerItems)
    // Łączna suma dnia.
    fun overallTotal(): Int = breakfastTotal() + lunchTotal() + dinnerTotal()
    // Parsowanie celu kalorii z tekstu do Int.
    fun calorieGoal(): Int = uiState.dailyCalorieGoal.toIntOrNull() ?: 0

    // Funkcja pomocnicza do podmiany jednego elementu listy bez mutowania starej listy.
    private fun List<MealItemData>.updatedAt(
        index: Int,
        transform: (MealItemData) -> MealItemData
    ): List<MealItemData> {
>>>>>>> Stashed changes
        return mapIndexed { currentIndex, currentValue ->
            if (currentIndex == index) value else currentValue
        }
    }

<<<<<<< Updated upstream
    private fun caloriesTotal(values: List<String>): Int {
        return values.sumOf { value -> value.toIntOrNull() ?: 0 }
=======
    // Liczymy sumę kalorii z listy pól tekstowych.
    private fun caloriesTotal(values: List<MealItemData>): Int {
        return values.sumOf { value -> value.calories.toIntOrNull() ?: 0 }
    }

    // Przykładowy trwały zapis do Room.
    fun insert(entry: FoodEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entry)
    }

    // Przykładowe trwałe usunięcie z Room.
    fun delete(entry: FoodEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(entry)
>>>>>>> Stashed changes
    }
}
