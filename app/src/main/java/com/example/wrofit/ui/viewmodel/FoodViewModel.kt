package com.example.wrofit.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.wrofit.data.dao.FoodDayDao
import com.example.wrofit.data.database.WroFitDatabase
import com.example.wrofit.data.model.FoodDayEntity
import com.example.wrofit.data.model.FoodEntry
import com.example.wrofit.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

data class MealItemData(
    val name: String = "",
    val calories: String = ""
)

data class FoodDayData(
    val breakfast: List<MealItemData> = List(3) { MealItemData() },
    val lunch: List<MealItemData> = List(3) { MealItemData() },
    val dinner: List<MealItemData> = List(3) { MealItemData() },
    val dailyCalorieGoal: String = "2000"
)

data class FoodUiState(
    val selectedDate: String = "",
    val breakfastExpanded: Boolean = true,
    val lunchExpanded: Boolean = false,
    val dinnerExpanded: Boolean = false,
    val dailyCalorieGoal: String = "2000",
    val breakfastItems: List<MealItemData> = List(3) { MealItemData() },
    val lunchItems: List<MealItemData> = List(3) { MealItemData() },
    val dinnerItems: List<MealItemData> = List(3) { MealItemData() }
)

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodRepository
    private val foodDayDao: FoodDayDao
    val allEntries: LiveData<List<FoodEntry>>
    val totalCalories: LiveData<Double>
    private var activeProfileId: String = ""

    // Mapa: Data -> Dane posiłków (zapewnia przypisanie do daty)
    private val _dailyFoodData = mutableStateMapOf<String, FoodDayData>()

    private val uiStateState: MutableState<FoodUiState> = mutableStateOf(FoodUiState())
    val uiState: FoodUiState
        get() = uiStateState.value

    init {
        val database = WroFitDatabase.getDatabase(application)
        repository = FoodRepository(database.foodDao())
        foodDayDao = database.foodDayDao()
        allEntries = repository.allEntries
        totalCalories = repository.totalCalories
    }

    // Ładowanie danych przy zmianie daty [cite: 285]
    fun setSelectedDate(date: String) {
        val profileId = activeProfileKey()
        val memoryKey = profileDateKey(profileId, date)
        uiStateState.value = uiState.copy(selectedDate = date)
        viewModelScope.launch {
            val dayData = withContext(Dispatchers.IO) {
                foodDayDao.getDay(profileId, date)?.toDayData()
            } ?: _dailyFoodData[memoryKey] ?: FoodDayData()

            if (uiState.selectedDate != date || activeProfileKey() != profileId) {
                return@launch
            }

            uiStateState.value = uiState.copy(
                selectedDate = date,
                dailyCalorieGoal = dayData.dailyCalorieGoal,
                breakfastItems = dayData.breakfast,
                lunchItems = dayData.lunch,
                dinnerItems = dayData.dinner
            )
            _dailyFoodData[memoryKey] = dayData
        }
    }

    fun setDailyCalorieGoal(value: String) {
        if (value.all(Char::isDigit)) {
            uiStateState.value = uiState.copy(dailyCalorieGoal = value)
            saveToMap()
        }
    }

    fun setActiveProfile(profileId: String) {
        activeProfileId = profileId
    }

    fun setBreakfastName(index: Int, value: String) {
        val sanitizedValue = value.filter { it.isLetter() || it.isWhitespace() }
        val updatedList = uiState.breakfastItems.updatedAt(index) { it.copy(name = sanitizedValue) }
        uiStateState.value = uiState.copy(breakfastItems = updatedList)
        saveToMap()
    }

    fun setBreakfastCalories(index: Int, value: String) {
        val updatedList = uiState.breakfastItems.updatedAt(index) { it.copy(calories = value) }
        uiStateState.value = uiState.copy(breakfastItems = updatedList)
        saveToMap()
    }

    fun setLunchName(index: Int, value: String) {
        val sanitizedValue = value.filter { it.isLetter() || it.isWhitespace() }
        val updatedList = uiState.lunchItems.updatedAt(index) { it.copy(name = sanitizedValue) }
        uiStateState.value = uiState.copy(lunchItems = updatedList)
        saveToMap()
    }

    fun setLunchCalories(index: Int, value: String) {
        val updatedList = uiState.lunchItems.updatedAt(index) { it.copy(calories = value) }
        uiStateState.value = uiState.copy(lunchItems = updatedList)
        saveToMap()
    }

    fun setDinnerName(index: Int, value: String) {
        val sanitizedValue = value.filter { it.isLetter() || it.isWhitespace() }
        val updatedList = uiState.dinnerItems.updatedAt(index) { it.copy(name = sanitizedValue) }
        uiStateState.value = uiState.copy(dinnerItems = updatedList)
        saveToMap()
    }

    fun setDinnerCalories(index: Int, value: String) {
        val updatedList = uiState.dinnerItems.updatedAt(index) { it.copy(calories = value) }
        uiStateState.value = uiState.copy(dinnerItems = updatedList)
        saveToMap()
    }

    private fun saveToMap() {
        val date = uiState.selectedDate
        val profileId = activeProfileKey()
        if (date.isNotBlank()) {
            val dayData = FoodDayData(
                breakfast = uiState.breakfastItems,
                lunch = uiState.lunchItems,
                dinner = uiState.dinnerItems,
                dailyCalorieGoal = uiState.dailyCalorieGoal
            )
            _dailyFoodData[profileDateKey(profileId, date)] = dayData
            viewModelScope.launch(Dispatchers.IO) {
                foodDayDao.upsert(dayData.toEntity(profileId, date))
            }
        }
    }

    private fun profileDateKey(profileId: String, date: String): String = "$profileId|$date"
    private fun activeProfileKey(): String = activeProfileId.ifBlank { "guest" }

    fun toggleBreakfast() {
        uiStateState.value = uiState.copy(breakfastExpanded = !uiState.breakfastExpanded)
    }

    fun toggleLunch() {
        uiStateState.value = uiState.copy(lunchExpanded = !uiState.lunchExpanded)
    }

    fun toggleDinner() {
        uiStateState.value = uiState.copy(dinnerExpanded = !uiState.dinnerExpanded)
    }

    fun breakfastTotal(): Int = caloriesTotal(uiState.breakfastItems)
    fun lunchTotal(): Int = caloriesTotal(uiState.lunchItems)
    fun dinnerTotal(): Int = caloriesTotal(uiState.dinnerItems)
    fun overallTotal(): Int = breakfastTotal() + lunchTotal() + dinnerTotal()
    fun calorieGoal(): Int = uiState.dailyCalorieGoal.toIntOrNull() ?: 0

    private fun List<MealItemData>.updatedAt(
        index: Int,
        transform: (MealItemData) -> MealItemData
    ): List<MealItemData> {
        return mapIndexed { currentIndex, currentValue ->
            if (currentIndex == index) transform(currentValue) else currentValue
        }
    }

    private fun caloriesTotal(values: List<MealItemData>): Int {
        return values.sumOf { value -> value.calories.toIntOrNull() ?: 0 }
    }

    private fun FoodDayData.toEntity(profileId: String, date: String): FoodDayEntity {
        return FoodDayEntity(
            profileId = profileId,
            date = date,
            breakfastJson = breakfast.toJson(),
            lunchJson = lunch.toJson(),
            dinnerJson = dinner.toJson(),
            dailyCalorieGoal = dailyCalorieGoal
        )
    }

    private fun FoodDayEntity.toDayData(): FoodDayData {
        return FoodDayData(
            breakfast = breakfastJson.toMealItems(),
            lunch = lunchJson.toMealItems(),
            dinner = dinnerJson.toMealItems(),
            dailyCalorieGoal = dailyCalorieGoal
        )
    }

    private fun List<MealItemData>.toJson(): String {
        val array = JSONArray()
        forEach { item ->
            array.put(
                JSONObject()
                    .put("name", item.name)
                    .put("calories", item.calories)
            )
        }
        return array.toString()
    }

    private fun String.toMealItems(): List<MealItemData> {
        return runCatching {
            val array = JSONArray(this)
            List(array.length()) { index ->
                val item = array.getJSONObject(index)
                MealItemData(
                    name = item.optString("name"),
                    calories = item.optString("calories")
                )
            }
        }.getOrDefault(List(3) { MealItemData() }).let { items ->
            if (items.size >= 3) items.take(3) else items + List(3 - items.size) { MealItemData() }
        }
    }

    fun insert(entry: FoodEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entry)
    }

    fun delete(entry: FoodEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(entry)
    }
}
