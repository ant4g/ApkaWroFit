package com.example.wrofit.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
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
    var showFoodDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            CustomBottomNavigation(selectedTab) { selectedTab = it }
        },
        floatingActionButton = {
            if (selectedTab == 1) {
                FloatingActionButton(onClick = { showFoodDialog = true }, backgroundColor = Color(0xFF4CAF50)) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> HomeScreen()     // [cite: 12, 17, 22]
                1 -> FoodScreen(entries, totalCalories, viewModel) // [cite: 13, 18, 80]
                2 -> SleepScreen()    // [cite: 14, 19, 107]
                3 -> ExerciseScreen() // [cite: 15, 20, 136]
                4 -> ProfileScreen()  // [cite: 16, 21, 190]
            }
        }
    }

    if (showFoodDialog) {
        AddFoodDialog(
            onDismiss = { showFoodDialog = false },
            onConfirm = { name, kcal, type ->
                val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
                viewModel.insert(FoodEntry(mealName = name, calories = kcal, mealType = type, date = date))
                showFoodDialog = false
            }
        )
    }
}

// --- 1. HOME SCREEN [cite: 22, 23, 24, 25, 26, 27] ---
@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("WroFit", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text("Witaj w aplikacji WroFit!", fontSize = 20.sp, color = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Text("Tutaj zadbasz o swoje zdrowie, kondycję i codzienne nawyki. Korzystaj z dostępnych zakładek, aby planować aktywność i dietę.")

        Spacer(Modifier.height(24.dp))
        Text("Poradniki", fontWeight = FontWeight.Bold, fontSize = 22.sp)

        TutorialCard("Ćwicz poprawnie", "Zobacz film instruktażowy ▷") // [cite: 44]
        TutorialCard("Sprawdź poprawne pozycje", "Zobacz galerię pozycji ▷") // [cite: 59]
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

// --- 2. FOOD SCREEN [cite: 80, 81, 82, 83] ---
@Composable
fun FoodScreen(entries: List<FoodEntry>, total: Double, viewModel: FoodViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Dziennik żywieniowy", modifier = Modifier.padding(16.dp), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp), backgroundColor = Color(0xFFF1F8E9)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Dzisiejsze kalorie: ${total.toInt()} kcal", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            }
        }
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(entries) { FoodRow(it) { viewModel.delete(it) } }
        }
    }
}

// --- 3. SLEEP SCREEN [cite: 107, 108, 110, 111, 112] ---
@Composable
fun SleepScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Harmonogram snu", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Wprowadź datę snu") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Godzina zaśnięcia") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Godzina przebudzenia") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Text("Twoja ilość snu wynosi: --", fontWeight = FontWeight.Bold)
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Twój cel snu") }, modifier = Modifier.fillMaxWidth())
    }
}

// --- 4. EXERCISE SCREEN [cite: 136, 137, 141, 163] ---
@Composable
fun ExerciseScreen() {
    val exercises = listOf("Pajacyki", "Przysiady", "Pompki", "Deska", "Wykroki", "Burpees")
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Stwórz swój plan na dzisiaj", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        LazyColumn {
            items(exercises) { ex ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Checkbox(checked = false, onCheckedChange = {})
                    Text(ex, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

// --- 5. PROFILE SCREEN  ---
@Composable
fun ProfileScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Twój profil", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Wprowadź datę urodzenia")
                Text("Data: --.--.----", color = Color.Gray)
            }
            Box(modifier = Modifier.size(100.dp).background(Color.LightGray), contentAlignment = Alignment.Center) {
                Text("Zdjęcie", fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(24.dp))
        Text("Informacje o Tobie", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        val fields = listOf("Imię i nazwisko", "Płeć", "Waga", "Wzrost", "Cel", "Poziom aktywności")
        fields.forEach { field ->
            OutlinedTextField(value = "", onValueChange = {}, label = { Text(field) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
        }
    }
}

// --- NAVIGATION & UTILS ---
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