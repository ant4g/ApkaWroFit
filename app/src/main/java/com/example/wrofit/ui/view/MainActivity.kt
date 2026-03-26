package com.example.wrofit.ui.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.viewinterop.AndroidView
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wrofit.data.model.GalleryImage
import com.example.wrofit.data.model.FoodEntry
import com.example.wrofit.ui.viewmodel.ExerciseViewModel
import com.example.wrofit.ui.viewmodel.FoodViewModel
import com.example.wrofit.ui.viewmodel.HomeViewModel
import com.example.wrofit.ui.viewmodel.NavigationViewModel
import com.example.wrofit.ui.viewmodel.ProfileViewModel
import com.example.wrofit.ui.viewmodel.SleepViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F9FA)) {
                    WroFitApp()
                }
            }
        }
    }
}

@Composable
fun WroFitApp() {
    val homeViewModel: HomeViewModel = viewModel()
    val navigationViewModel: NavigationViewModel = viewModel()
    val foodViewModel: FoodViewModel = viewModel()
    val sleepViewModel: SleepViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    Scaffold(
        bottomBar = {
            CustomBottomNavigation(navigationViewModel.selectedTab, navigationViewModel::updateSelectedTab)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .safeDrawingPadding()
        ) {
            when (navigationViewModel.selectedTab) {
                0 -> HomeScreen(homeViewModel)
                1 -> FoodScreen(foodViewModel)
                2 -> SleepScreen(sleepViewModel)
                3 -> ExerciseScreen(exerciseViewModel)
                4 -> ProfileScreen(profileViewModel)
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("WroFit", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text("Witaj w aplikacji WroFit!", fontSize = 20.sp, color = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Text("Tutaj zadbasz o swoje zdrowie, kondycję i codzienne nawyki. Korzystaj z dostępnych zakładek, aby planować aktywność i dietę.")

        Spacer(Modifier.height(24.dp))
        Text("Poradniki", fontWeight = FontWeight.Bold, fontSize = 22.sp)

        TutorialCard("Ćwicz poprawnie", "Zobacz film instruktażowy", onClick = viewModel::showTutorialVideo)
        TutorialCard("Sprawdź poprawne pozycje", "Zobacz galerię pozycji", onClick = viewModel::showPositionsGallery)
    }

    if (viewModel.isTutorialVideoVisible) {
        TutorialVideoDialog(onDismiss = viewModel::hideTutorialVideo)
    }

    if (viewModel.isPositionsGalleryVisible) {
        PositionsGalleryDialog(viewModel = viewModel, onDismiss = viewModel::hidePositionsGallery)
    }
}

@Composable
fun TutorialCard(title: String, buttonText: String, onClick: () -> Unit = {}) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), shape = RoundedCornerShape(12.dp), backgroundColor = Color(0xFFE0E0E0)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontWeight = FontWeight.Medium)
            Button(onClick = onClick, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black), shape = RoundedCornerShape(8.dp)) {
                Text(buttonText, color = Color.White)
            }
        }
    }
}

@Composable
fun TutorialVideoDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val videoResourceId = remember {
        context.resources.getIdentifier("home_tutorial_video", "raw", context.packageName)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Film instruktażowy",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onDismiss) {
                        Text("Zamknij", color = Color.White)
                    }
                }

                Spacer(Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (videoResourceId != 0) {
                        AndroidView(
                            factory = { viewContext ->
                                VideoView(viewContext).apply {
                                    val mediaController = MediaController(viewContext)
                                    mediaController.setAnchorView(this)
                                    setMediaController(mediaController)
                                    setVideoPath("android.resource://${viewContext.packageName}/$videoResourceId")
                                    setOnPreparedListener { player ->
                                        player.isLooping = false
                                        start()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            "Nie znaleziono pliku filmu. Dodaj plik `home_tutorial_video.mp4` do folderu `app/src/main/res/raw`.",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PositionsGalleryDialog(viewModel: HomeViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val galleryItems by viewModel.galleryImages.observeAsState(initial = emptyList())

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F4F4))
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Galeria pozycji",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onDismiss) {
                        Text("Zamknij")
                    }
                }

                Spacer(Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    gridItems(galleryItems) { item ->
                        GalleryImageCard(item = item, context = context)
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryImageCard(item: GalleryImage, context: android.content.Context) {
    val drawableId = remember(item.drawableName) {
        context.resources.getIdentifier(item.drawableName, "drawable", context.packageName)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 3.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFECECEC))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFFF7F7F7)),
                contentAlignment = Alignment.Center
            ) {
                if (drawableId != 0) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = item.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "Dodaj obraz:\n${item.drawableName}.png",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FoodScreen(viewModel: FoodViewModel) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val inputFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val polishFormatter = remember { SimpleDateFormat("EEEE, d MMMM", Locale("pl", "PL")) }

    LaunchedEffect(Unit) {
        if (uiState.selectedDate.isBlank()) {
            viewModel.setSelectedDate(inputFormatter.format(calendar.time))
        }
    }

    val mealDateLabel = remember(uiState.selectedDate) {
        runCatching { inputFormatter.parse(uiState.selectedDate) }
            .getOrNull()
            ?.let { "[ ${polishFormatter.format(it).uppercase(Locale("pl", "PL"))} ]" }
            ?: ""
    }

    val openDatePicker = {
        runCatching { inputFormatter.parse(uiState.selectedDate) }
            .getOrNull()
            ?.let { calendar.time = it }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                viewModel.setSelectedDate(inputFormatter.format(calendar.time))
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
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = openDatePicker),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, Color(0xFFBDBDBD)),
            color = Color.White,
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (uiState.selectedDate.isBlank()) "Data" else uiState.selectedDate,
                    modifier = Modifier.weight(1f),
                    color = if (uiState.selectedDate.isBlank()) Color.Gray else Color.Black
                )
                Icon(Icons.Default.DateRange, contentDescription = "Wybierz datę")
            }
        }
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
                expanded = uiState.breakfastExpanded,
                onToggle = viewModel::toggleBreakfast,
                values = uiState.breakfastCalories,
                onValueChange = { index, value ->
                    viewModel.setBreakfastCalories(index, value)
                },
                dateLabel = mealDateLabel
            )
            Spacer(Modifier.height(28.dp))
            MealSectionCard(
                title = "Obiad",
                expanded = uiState.lunchExpanded,
                onToggle = viewModel::toggleLunch,
                values = uiState.lunchCalories,
                onValueChange = { index, value ->
                    viewModel.setLunchCalories(index, value)
                },
                dateLabel = mealDateLabel
            )
            Spacer(Modifier.height(12.dp))
            MealSectionCard(
                title = "Kolacja",
                expanded = uiState.dinnerExpanded,
                onToggle = viewModel::toggleDinner,
                values = uiState.dinnerCalories,
                onValueChange = { index, value ->
                    viewModel.setDinnerCalories(index, value)
                },
                dateLabel = mealDateLabel
            )
            Spacer(Modifier.height(24.dp))
            Text("Suma Śniadanie: ${viewModel.breakfastTotal()} kcal", fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text("Suma Obiad: ${viewModel.lunchTotal()} kcal", fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text("Suma Kolacja: ${viewModel.dinnerTotal()} kcal", fontSize = 16.sp)
            Spacer(Modifier.height(14.dp))
            Text("Suma całościowa: ${viewModel.overallTotal()} kcal", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
fun SleepScreen(viewModel: SleepViewModel) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    LaunchedEffect(Unit) {
        if (uiState.selectedDate.isBlank()) {
            viewModel.setSelectedDate(dateFormatter.format(calendar.time))
        }
    }

    val openDatePicker = {
        runCatching { dateFormatter.parse(uiState.selectedDate) }
            .getOrNull()
            ?.let { calendar.time = it }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                viewModel.setSelectedDate(dateFormatter.format(calendar.time))
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

    val sleepDurationText = remember(uiState.sleepTime, uiState.wakeTime) {
        val sleepParts = uiState.sleepTime.split(":")
        val wakeParts = uiState.wakeTime.split(":")
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
            value = uiState.selectedDate,
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
            value = uiState.sleepTime,
            onValueChange = {},
            readOnly = true,
            label = { Text("Godzina zaśnięcia") },
            trailingIcon = {
                IconButton(onClick = { openTimePicker(uiState.sleepTime) { viewModel.setSleepTime(it) } }) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Wybierz godzinę zaśnięcia")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.wakeTime,
            onValueChange = {},
            readOnly = true,
            label = { Text("Godzina przebudzenia") },
            trailingIcon = {
                IconButton(onClick = { openTimePicker(uiState.wakeTime) { viewModel.setWakeTime(it) } }) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Wybierz godzinę przebudzenia")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Text("Twoja ilość snu wynosi: $sleepDurationText", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = uiState.difficulties,
            onValueChange = { viewModel.setDifficulties(it) },
            label = { Text("Zaistniałe trudności") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ExerciseScreen(viewModel: ExerciseViewModel) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    LaunchedEffect(Unit) {
        if (uiState.selectedDate.isBlank()) {
            viewModel.setSelectedDate(dateFormatter.format(calendar.time))
        }
    }

    val openDatePicker = {
        runCatching { dateFormatter.parse(uiState.selectedDate) }
            .getOrNull()
            ?.let { calendar.time = it }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                viewModel.setSelectedDate(dateFormatter.format(calendar.time))
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
                value = uiState.selectedDate,
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
            OutlinedButton(onClick = viewModel::resetCheckedExercises) {
                Icon(Icons.Default.Refresh, contentDescription = "Resetuj zaznaczenia")
                Spacer(Modifier.width(6.dp))
                Text("Reset")
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(viewModel.exercises) { ex ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Checkbox(
                        checked = ex in uiState.checkedExercises,
                        onCheckedChange = { isChecked ->
                            viewModel.toggleExercise(ex, isChecked)
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
fun ProfileScreen(viewModel: ProfileViewModel) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val calendar = remember { Calendar.getInstance() }

    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    LaunchedEffect(Unit) {
        if (uiState.selectedDate.isBlank()) {
            viewModel.setSelectedDate(dateFormatter.format(calendar.time))
        }
    }

    val openDatePicker = {
        runCatching { dateFormatter.parse(uiState.selectedDate) }
            .getOrNull()
            ?.let { calendar.time = it }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                viewModel.setSelectedDate(dateFormatter.format(calendar.time))
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
                        value = uiState.selectedDate,
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
                            viewModel.setSelectedDate(dateFormatter.format(calendar.time))
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
            value = uiState.fullName,
            onValueChange = viewModel::setFullName,
            label = { Text("Imię i nazwisko") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = uiState.genderExpanded,
            onExpandedChange = {
                focusManager.clearFocus()
                viewModel.setGenderExpanded(!uiState.genderExpanded)
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            OutlinedTextField(
                value = uiState.gender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Płeć") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.genderExpanded) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = uiState.genderExpanded,
                onDismissRequest = { viewModel.setGenderExpanded(false) }
            ) {
                viewModel.genderOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        viewModel.setGender(option)
                    }) {
                        Text(option)
                    }
                }
            }
        }

        OutlinedTextField(
            value = uiState.weight,
            onValueChange = viewModel::setWeight,
            label = { Text("Waga") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = uiState.height,
            onValueChange = viewModel::setHeight,
            label = { Text("Wzrost") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = uiState.goal,
            onValueChange = viewModel::setGoal,
            label = { Text("Cel") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = uiState.activityExpanded,
            onExpandedChange = {
                focusManager.clearFocus()
                viewModel.setActivityExpanded(!uiState.activityExpanded)
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            OutlinedTextField(
                value = uiState.activityLevel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Poziom aktywności") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.activityExpanded) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = uiState.activityExpanded,
                onDismissRequest = { viewModel.setActivityExpanded(false) }
            ) {
                viewModel.activityOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        viewModel.setActivityLevel(option)
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
