package com.example.wrofit.ui.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wrofit.data.model.FoodEntry
import com.example.wrofit.ui.viewmodel.FoodViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F9FA)) {
                    val foodViewModel: FoodViewModel = viewModel()
                    WroFitApp(foodViewModel)
                }
            }
        }
    }
}

@Composable
fun WroFitApp(viewModel: FoodViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val entries by viewModel.allEntries.observeAsState(initial = emptyList())
    val totalCalories by viewModel.totalCalories.observeAsState(initial = 0.0)

    Scaffold(
        bottomBar = {
            CustomBottomNavigation(selectedTab) { selectedTab = it }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .safeDrawingPadding()
        ) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> FoodScreen(entries, totalCalories, viewModel)
                2 -> SleepScreen()
                3 -> ExerciseScreen()
                4 -> ProfileScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("WroFit", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text("Witaj w aplikacji WroFit!", fontSize = 20.sp, color = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Text("Tutaj zadbasz o swoje zdrowie, kondycję i codzienne nawyki. Korzystaj z dostępnych zakładek, aby planować aktywność i dietę.")

        Spacer(Modifier.height(24.dp))
        Text("Poradniki", fontWeight = FontWeight.Bold, fontSize = 22.sp)

        TutorialCard("Ćwicz poprawnie", "Zobacz film instruktażowy")
        TutorialCard("Sprawdź poprawne pozycje", "Zobacz galerię pozycji")
    }
}

@Composable
fun TutorialCard(title: String, buttonText: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), shape = RoundedCornerShape(12.dp), backgroundColor = Color(0xFFE0E0E0)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontWeight = FontWeight.Medium)
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black), shape = RoundedCornerShape(8.dp)) {
                Text(buttonText, color = Color.White)
            }
        }
    }
}

@Composable
fun FoodScreen(entries: List<FoodEntry>, total: Double, viewModel: FoodViewModel) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val inputFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val polishFormatter = remember { SimpleDateFormat("EEEE, d MMMM", Locale("pl", "PL")) }
    var selectedDate by remember { mutableStateOf(inputFormatter.format(calendar.time)) }
    var breakfastExpanded by remember { mutableStateOf(true) }
    var lunchExpanded by remember { mutableStateOf(false) }
    var dinnerExpanded by remember { mutableStateOf(false) }
    var breakfastKcal1 by remember { mutableStateOf("") }
    var breakfastKcal2 by remember { mutableStateOf("") }
    var breakfastKcal3 by remember { mutableStateOf("") }
    var lunchKcal1 by remember { mutableStateOf("") }
    var lunchKcal2 by remember { mutableStateOf("") }
    var lunchKcal3 by remember { mutableStateOf("") }
    var dinnerKcal1 by remember { mutableStateOf("") }
    var dinnerKcal2 by remember { mutableStateOf("") }
    var dinnerKcal3 by remember { mutableStateOf("") }

    val mealDateLabel = remember(selectedDate) {
        runCatching { inputFormatter.parse(selectedDate) }
            .getOrNull()
            ?.let { "[ ${polishFormatter.format(it).uppercase(Locale("pl", "PL"))} ]" }
            ?: ""
    }
    val breakfastTotal = listOf(breakfastKcal1, breakfastKcal2, breakfastKcal3).sumOf { it.toIntOrNull() ?: 0 }
    val lunchTotal = listOf(lunchKcal1, lunchKcal2, lunchKcal3).sumOf { it.toIntOrNull() ?: 0 }
    val dinnerTotal = listOf(dinnerKcal1, dinnerKcal2, dinnerKcal3).sumOf { it.toIntOrNull() ?: 0 }
    val overallTotal = breakfastTotal + lunchTotal + dinnerTotal

    val openDatePicker = {
        runCatching { inputFormatter.parse(selectedDate) }
            .getOrNull()
            ?.let { calendar.time = it }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = inputFormatter.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Wprowadź datę posiłku",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 12.dp),
            fontSize = 18.sp
        )
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Data") },
            trailingIcon = {
                IconButton(onClick = openDatePicker) {
                    Icon(Icons.Default.DateRange, contentDescription = "Wybierz datę")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Divider(
            modifier = Modifier.padding(top = 16.dp),
            color = Color.Black,
            thickness = 2.dp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 22.dp)
        ) {
            MealSectionCard(
                title = "Śniadanie",
                expanded = breakfastExpanded,
                onToggle = { breakfastExpanded = !breakfastExpanded },
                values = listOf(breakfastKcal1, breakfastKcal2, breakfastKcal3),
                onValueChange = { index, value ->
                    when (index) {
                        0 -> breakfastKcal1 = value
                        1 -> breakfastKcal2 = value
                        else -> breakfastKcal3 = value
                    }
                },
                dateLabel = mealDateLabel
            )
            Spacer(Modifier.height(28.dp))
            MealSectionCard(
                title = "Obiad",
                expanded = lunchExpanded,
                onToggle = { lunchExpanded = !lunchExpanded },
                values = listOf(lunchKcal1, lunchKcal2, lunchKcal3),
                onValueChange = { index, value ->
                    when (index) {
                        0 -> lunchKcal1 = value
                        1 -> lunchKcal2 = value
                        else -> lunchKcal3 = value
                    }
                },
                dateLabel = mealDateLabel
            )
            Spacer(Modifier.height(12.dp))
            MealSectionCard(
                title = "Kolacja",
                expanded = dinnerExpanded,
                onToggle = { dinnerExpanded = !dinnerExpanded },
                values = listOf(dinnerKcal1, dinnerKcal2, dinnerKcal3),
                onValueChange = { index, value ->
                    when (index) {
                        0 -> dinnerKcal1 = value
                        1 -> dinnerKcal2 = value
                        else -> dinnerKcal3 = value
                    }
                },
                dateLabel = mealDateLabel
            )
            Spacer(Modifier.height(24.dp))
            Text("Suma Śniadanie: $breakfastTotal kcal", fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text("Suma Obiad: $lunchTotal kcal", fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text("Suma Kolacja: $dinnerTotal kcal", fontSize = 16.sp)
            Spacer(Modifier.height(14.dp))
            Text("Suma całościowa: $overallTotal kcal", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MealSectionCard(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    values: List<String>,
    onValueChange: (Int, String) -> Unit,
    dateLabel: String
) {
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFD6D6D6)),
            color = Color.White,
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp
                )
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }

        if (expanded) {
            Spacer(Modifier.height(14.dp))
            Text(
                dateLabel,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp
            )
            Divider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color.Black.copy(alpha = 0.75f),
                thickness = 1.dp
            )
            values.forEachIndexed { index, value ->
                KcalInputRow(
                    label = "Posiłek ${index + 1}:",
                    value = value,
                    onValueChange = { onValueChange(index, it) }
                )
                Spacer(Modifier.height(12.dp))
            }
            Divider(
                modifier = Modifier.padding(top = 10.dp),
                color = Color.Black.copy(alpha = 0.75f),
                thickness = 1.dp
            )
        }
    }
}

@Composable
fun KcalInputRow(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { input -> onValueChange(input.filter { it.isDigit() }) },
        label = { Text(label) },
        placeholder = { Text("X kcal") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun SleepScreen() {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    var selectedDate by remember { mutableStateOf(dateFormatter.format(calendar.time)) }
    var sleepTime by remember { mutableStateOf("") }
    var wakeTime by remember { mutableStateOf("") }
    var difficulties by remember { mutableStateOf("") }

    val openDatePicker = {
        runCatching { dateFormatter.parse(selectedDate) }
            .getOrNull()
            ?.let { calendar.time = it }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = dateFormatter.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun openTimePicker(initialValue: String, onTimeSelected: (String) -> Unit) {
        val parts = initialValue.split(":")
        val initialHour = parts.getOrNull(0)?.toIntOrNull() ?: calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = parts.getOrNull(1)?.toIntOrNull() ?: calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                onTimeSelected(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute))
            },
            initialHour,
            initialMinute,
            true
        ).show()
    }

    val sleepDurationText = remember(sleepTime, wakeTime) {
        val sleepParts = sleepTime.split(":")
        val wakeParts = wakeTime.split(":")
        val sleepHour = sleepParts.getOrNull(0)?.toIntOrNull()
        val sleepMinute = sleepParts.getOrNull(1)?.toIntOrNull()
        val wakeHour = wakeParts.getOrNull(0)?.toIntOrNull()
        val wakeMinute = wakeParts.getOrNull(1)?.toIntOrNull()

        if (sleepHour == null || sleepMinute == null || wakeHour == null || wakeMinute == null) {
            "--"
        } else {
            val sleepMinutes = sleepHour * 60 + sleepMinute
            var wakeMinutes = wakeHour * 60 + wakeMinute
            if (wakeMinutes < sleepMinutes) {
                wakeMinutes += 24 * 60
            }
            val duration = wakeMinutes - sleepMinutes
            val hours = duration / 60
            val minutes = duration % 60
            "${hours} h ${minutes} min"
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Harmonogram snu", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Data snu") },
            trailingIcon = {
                IconButton(onClick = openDatePicker) {
                    Icon(Icons.Default.DateRange, contentDescription = "Wybierz datę snu")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = sleepTime,
            onValueChange = {},
            readOnly = true,
            label = { Text("Godzina zaśnięcia") },
            trailingIcon = {
                IconButton(onClick = { openTimePicker(sleepTime) { sleepTime = it } }) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Wybierz godzinę zaśnięcia")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = wakeTime,
            onValueChange = {},
            readOnly = true,
            label = { Text("Godzina przebudzenia") },
            trailingIcon = {
                IconButton(onClick = { openTimePicker(wakeTime) { wakeTime = it } }) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Wybierz godzinę przebudzenia")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Text("Twoja ilość snu wynosi: $sleepDurationText", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = difficulties,
            onValueChange = { difficulties = it },
            label = { Text("Zaistniałe trudności") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ExerciseScreen() {
    val exercises = listOf("Pajacyki", "Przysiady", "Pompki", "Deska", "Wykroki", "Burpees")
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    var selectedDate by remember { mutableStateOf(dateFormatter.format(calendar.time)) }
    var checkedExercises by remember { mutableStateOf(setOf<String>()) }

    val openDatePicker = {
        runCatching { dateFormatter.parse(selectedDate) }
            .getOrNull()
            ?.let { calendar.time = it }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = dateFormatter.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Stwórz swój plan na dzisiaj", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Data treningu") },
                trailingIcon = {
                    IconButton(onClick = openDatePicker) {
                        Icon(Icons.Default.DateRange, contentDescription = "Wybierz datę treningu")
                    }
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = { checkedExercises = emptySet() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Resetuj zaznaczenia")
                Spacer(Modifier.width(6.dp))
                Text("Reset")
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(exercises) { ex ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Checkbox(
                        checked = ex in checkedExercises,
                        onCheckedChange = { isChecked ->
                            checkedExercises = if (isChecked) {
                                checkedExercises + ex
                            } else {
                                checkedExercises - ex
                            }
                        }
                    )
                    Text(ex, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val calendar = remember { Calendar.getInstance() }
    var selectedDate by remember {
        mutableStateOf(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time))
    }
    var fullName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf("") }
    var genderExpanded by remember { mutableStateOf(false) }
    var activityExpanded by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val genderOptions = listOf("Kobieta", "Mężczyzna", "Wolę nie podawać")
    val activityOptions = listOf("Wysoki", "Średni", "Niski", "Brak - siedzący")
    val openDatePicker = {
        runCatching { dateFormatter.parse(selectedDate) }
            .getOrNull()
            ?.let { calendar.time = it }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = dateFormatter.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Twój profil", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Wprowadź datę urodzenia")
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Data urodzenia") },
                        trailingIcon = {
                            IconButton(onClick = openDatePicker) {
                                Icon(Icons.Default.DateRange, contentDescription = "Wybierz datę z kalendarza")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            calendar.time = Date()
                            selectedDate = dateFormatter.format(calendar.time)
                        }
                    ) {
                        Icon(Icons.Default.Today, contentDescription = "Ustaw dzisiejszą datę")
                    }
                }
            }
            Box(modifier = Modifier.size(100.dp).background(Color.LightGray), contentAlignment = Alignment.Center) {
                Text("Zdjęcie", fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(24.dp))
        Text("Informacje o Tobie", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = fullName,
            onValueChange = { input ->
                if (input.all { it.isLetter() || it.isWhitespace() }) {
                    fullName = input
                }
            },
            label = { Text("Imię i nazwisko") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = {
                focusManager.clearFocus()
                genderExpanded = !genderExpanded
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            OutlinedTextField(
                value = gender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Płeć") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                genderOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        gender = option
                        genderExpanded = false
                    }) {
                        Text(option)
                    }
                }
            }
        }

        OutlinedTextField(
            value = weight,
            onValueChange = { input ->
                if (input.all { it.isDigit() }) {
                    weight = input
                }
            },
            label = { Text("Waga") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = height,
            onValueChange = { input ->
                if (input.all { it.isDigit() }) {
                    height = input
                }
            },
            label = { Text("Wzrost") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = goal,
            onValueChange = { input ->
                if (input.all { it.isLetter() || it.isWhitespace() }) {
                    goal = input
                }
            },
            label = { Text("Cel") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = activityExpanded,
            onExpandedChange = {
                focusManager.clearFocus()
                activityExpanded = !activityExpanded
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            OutlinedTextField(
                value = activityLevel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Poziom aktywności") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = activityExpanded) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = activityExpanded,
                onDismissRequest = { activityExpanded = false }
            ) {
                activityOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        activityLevel = option
                        activityExpanded = false
                    }) {
                        Text(option)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomBottomNavigation(selected: Int, onSelected: (Int) -> Unit) {
    Card(modifier = Modifier.padding(16.dp).height(70.dp).fillMaxWidth(), shape = RoundedCornerShape(35.dp), elevation = 8.dp) {
        BottomNavigation(backgroundColor = Color.White, elevation = 0.dp) {
            val items = listOf(
                Triple(Icons.Default.Home, "Home", 0),
                Triple(Icons.Default.ShoppingCart, "Food", 1),
                Triple(Icons.Default.NightsStay, "Sleep", 2),
                Triple(Icons.Default.FlashOn, "Exercise", 3),
                Triple(Icons.Default.Person, "Profile", 4)
            )
            items.forEach { (icon, label, index) ->
                BottomNavigationItem(
                    icon = { Icon(icon, null) },
                    label = { Text(label, fontSize = 10.sp) },
                    selected = selected == index,
                    onClick = { onSelected(index) },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun FoodRow(entry: FoodEntry, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), elevation = 1.dp) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.mealName, fontWeight = FontWeight.Bold)
                Text("${entry.mealType} • ${entry.date}", fontSize = 12.sp, color = Color.Gray)
            }
            Text("${entry.calories.toInt()} kcal", fontWeight = FontWeight.Bold)
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
        }
    }
}

@Composable
fun AddFoodDialog(onDismiss: () -> Unit, onConfirm: (String, Double, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var kcal by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dodaj posiłek") },
        text = {
            Column {
                TextField(name, { name = it }, label = { Text("Nazwa") })
                TextField(kcal, { kcal = it }, label = { Text("Kalorie") })
                TextField(type, { type = it }, label = { Text("Typ (np. Obiad)") })
            }
        },
        confirmButton = { Button(onClick = { onConfirm(name, kcal.toDoubleOrNull() ?: 0.0, type) }) { Text("DODAJ") } }
    )
}
