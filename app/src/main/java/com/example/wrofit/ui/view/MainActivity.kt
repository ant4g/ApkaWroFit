package com.example.wrofit.ui.view

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wrofit.R
import com.example.wrofit.data.model.GalleryImage
import com.example.wrofit.data.model.TutorialVideo
import com.example.wrofit.ui.viewmodel.ExerciseViewModel
import com.example.wrofit.ui.viewmodel.FoodViewModel
import com.example.wrofit.ui.viewmodel.HomeViewModel
import com.example.wrofit.ui.viewmodel.MealItemData
import com.example.wrofit.ui.viewmodel.NavigationViewModel
import com.example.wrofit.ui.viewmodel.ProfileViewModel
import com.example.wrofit.ui.viewmodel.SleepViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F7FB)
                ) {
                    WroFitApp()
                }
            }
        }
    }
}

@Composable
fun WroFitApp() {
    val navViewModel: NavigationViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val foodViewModel: FoodViewModel = viewModel()
    val sleepViewModel: SleepViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val activeProfileId = profileViewModel.uiState.profileId

    LaunchedEffect(navViewModel.selectedDate, activeProfileId) {
        val date = navViewModel.selectedDate
        homeViewModel.updateSelectedDate(date)
        foodViewModel.setActiveProfile(activeProfileId)
        foodViewModel.setSelectedDate(date)
        sleepViewModel.setActiveProfile(activeProfileId)
        sleepViewModel.setSelectedDate(date)
        exerciseViewModel.setActiveProfile(activeProfileId)
        exerciseViewModel.setSelectedDate(date)
    }

    Scaffold(
        backgroundColor = Color(0xFFF5F7FB),
        bottomBar = {
            CustomBottomNavigation(
                selected = navViewModel.selectedTab,
                onSelected = navViewModel::updateSelectedTab
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (navViewModel.selectedTab) {
                0 -> HomeScreen(homeViewModel, navViewModel)
                1 -> FoodScreen(foodViewModel, navViewModel)
                2 -> SleepScreen(sleepViewModel, navViewModel)
                3 -> ExerciseScreen(exerciseViewModel, navViewModel)
                4 -> ProfileScreen(profileViewModel)
            }
        }
    }
}

@Composable
fun CustomBottomNavigation(selected: Int, onSelected: (Int) -> Unit) {
    val items = listOf(
        Triple("Home", Icons.Default.Home, 0),
        Triple("Jedzenie", Icons.Default.Restaurant, 1),
        Triple("Sen", Icons.Default.Bedtime, 2),
        Triple("Ruch", Icons.Default.FitnessCenter, 3),
        Triple("Profil", Icons.Default.Person, 4)
    )

    BottomNavigation(backgroundColor = Color.White, elevation = 10.dp) {
        items.forEach { (label, icon, index) ->
            BottomNavigationItem(
                selected = selected == index,
                onClick = { onSelected(index) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                selectedContentColor = Color(0xFF1D4ED8),
                unselectedContentColor = Color(0xFF64748B)
            )
        }
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel, navViewModel: NavigationViewModel) {
    val galleryImages by viewModel.galleryImages.observeAsState(emptyList())
    val tutorialVideo by viewModel.tutorialVideo.observeAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ScreenHeader(
                title = "WroFit",
                subtitle = "Twoje codzienne wsparcie zdrowych nawykow",
                selectedDate = navViewModel.selectedDate,
                onDateSelected = navViewModel::updateSelectedDate
            )
        }

        item {
            TutorialCard(
                tutorialVideo = tutorialVideo,
                onOpen = viewModel::showTutorialVideo
            )
        }

        if (galleryImages.isNotEmpty()) {
            item {
                GalleryPreview(
                    images = galleryImages.take(4),
                    onOpen = viewModel::showPositionsGallery
                )
            }
        }
    }

    if (viewModel.isTutorialVideoVisible) {
        TutorialVideoDialog(
            tutorialVideo = tutorialVideo,
            onDismiss = viewModel::hideTutorialVideo
        )
    }

    if (viewModel.isPositionsGalleryVisible) {
        PositionsGalleryDialog(
            images = galleryImages,
            onDismiss = viewModel::hidePositionsGallery
        )
    }
}

@Composable
fun FoodScreen(viewModel: FoodViewModel, navViewModel: NavigationViewModel) {
    val uiState = viewModel.uiState
    val totalCalories = viewModel.overallTotal()
    val calorieGoal = viewModel.calorieGoal()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                title = "Kalorie",
                subtitle = "Uzupelniaj posilki dla wybranego dnia",
                selectedDate = navViewModel.selectedDate,
                onDateSelected = navViewModel::updateSelectedDate
            )
        }

        item {
            GoalInputCard(
                title = "Max kalorii na dzien",
                value = uiState.dailyCalorieGoal,
                suffix = "kcal",
                onValueChange = viewModel::setDailyCalorieGoal
            )
        }

        item {
            ProgressDonutCard(
                title = "Postep kalorii",
                currentValue = totalCalories.toFloat(),
                goalValue = calorieGoal.toFloat(),
                primaryLabel = "$totalCalories kcal",
                secondaryLabel = if (calorieGoal > 0) "Cel: $calorieGoal kcal" else "Ustaw max kalorii"
            )
        }

        item {
            MealSection(
                title = "Sniadanie",
                items = uiState.breakfastItems,
                expanded = uiState.breakfastExpanded,
                total = viewModel.breakfastTotal(),
                onToggle = viewModel::toggleBreakfast,
                onNameChange = viewModel::setBreakfastName,
                onValueChange = viewModel::setBreakfastCalories
            )
        }

        item {
            MealSection(
                title = "Obiad",
                items = uiState.lunchItems,
                expanded = uiState.lunchExpanded,
                total = viewModel.lunchTotal(),
                onToggle = viewModel::toggleLunch,
                onNameChange = viewModel::setLunchName,
                onValueChange = viewModel::setLunchCalories
            )
        }

        item {
            MealSection(
                title = "Kolacja",
                items = uiState.dinnerItems,
                expanded = uiState.dinnerExpanded,
                total = viewModel.dinnerTotal(),
                onToggle = viewModel::toggleDinner,
                onNameChange = viewModel::setDinnerName,
                onValueChange = viewModel::setDinnerCalories
            )
        }

        item {
            InfoCard(
                title = "Laczna ilosc kalorii w ciagu dnia",
                body = "$totalCalories kcal"
            )
        }
    }
}

@Composable
fun SleepScreen(viewModel: SleepViewModel, navViewModel: NavigationViewModel) {
    val uiState = viewModel.uiState
    val sleptHours = viewModel.sleptHours()
    val sleepGoal = viewModel.sleepGoal()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenHeader(
            title = "Sen",
            subtitle = "Zapisz godziny i notatki dla wybranego dnia",
            selectedDate = navViewModel.selectedDate,
            onDateSelected = navViewModel::updateSelectedDate
        )

        TimePickerField(
            label = "Godzina snu",
            value = uiState.sleepTime,
            onValueChange = viewModel::setSleepTime,
            modifier = Modifier.fillMaxWidth()
        )

        TimePickerField(
            label = "Godzina pobudki",
            value = uiState.wakeTime,
            onValueChange = viewModel::setWakeTime,
            modifier = Modifier.fillMaxWidth()
        )

        GoalInputCard(
            title = "Cel godzinowy snu",
            value = uiState.sleepGoalHours,
            suffix = "godziny",
            onValueChange = viewModel::setSleepGoalHours
        )

        ProgressDonutCard(
            title = "Postep snu",
            currentValue = sleptHours.toFloat(),
            goalValue = sleepGoal.toFloat(),
            primaryLabel = "${formatOneDecimal(sleptHours)} h",
            secondaryLabel = if (sleepGoal > 0) "Cel: $sleepGoal h" else "Ustaw cel godzinowy"
        )

        LabeledTextField(
            label = "Trudnosci ze snem",
            value = uiState.difficulties,
            onValueChange = viewModel::setDifficulties,
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            minLines = 4
        )
    }
}

@Composable
fun ExerciseScreen(viewModel: ExerciseViewModel, navViewModel: NavigationViewModel) {
    val uiState = viewModel.uiState
    val orderedExercises = remember(viewModel.exercises, uiState.checkedExercises) {
        viewModel.exercises.sortedByDescending { it in uiState.checkedExercises }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                title = "Aktywnosc",
                subtitle = "Odhacz wykonane cwiczenia",
                selectedDate = navViewModel.selectedDate,
                onDateSelected = navViewModel::updateSelectedDate,
                actionContent = {
                    HeaderSquareAction(
                        label = "Wyczysc",
                        onClick = viewModel::resetCheckedExercises
                    )
                }
            )
        }

        items(orderedExercises) { exercise ->
            Card(shape = RoundedCornerShape(16.dp), elevation = 3.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = exercise in uiState.checkedExercises,
                        onCheckedChange = { checked -> viewModel.toggleExercise(exercise, checked) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = exercise, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val uiState = viewModel.uiState
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.setProfilePhotoUri(uri.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (uiState.isProfileSaved) {
            SavedProfileCard(
                uiState = uiState,
                onEdit = viewModel::editProfile,
                onDelete = viewModel::deleteProfile
            )
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                backgroundColor = Color.White,
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfilePhoto(
                        profilePhotoUri = uiState.profilePhotoUri,
                        onChoosePhoto = { photoPicker.launch("image/*") }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Profil",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Miejsce na zdjecie profilowe i podstawowe dane uzytkownika.",
                        color = Color(0xFF475569),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { photoPicker.launch("image/*") }) {
                        Text("Dodaj zdjecie profilowe")
                    }
                }
            }

            LabeledTextField(
                label = "Imie i nazwisko",
                value = uiState.fullName,
                onValueChange = viewModel::setFullName,
                modifier = Modifier.fillMaxWidth()
            )

            ProfileDropdown(
                label = "Plec",
                value = uiState.gender,
                expanded = uiState.genderExpanded,
                options = viewModel.genderOptions,
                onExpandedChange = viewModel::setGenderExpanded,
                onOptionSelected = viewModel::setGender
            )

            LabeledTextField(
                label = "Waga (kg)",
                value = uiState.weight,
                onValueChange = viewModel::setWeight,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number
            )

            LabeledTextField(
                label = "Wzrost (cm)",
                value = uiState.height,
                onValueChange = viewModel::setHeight,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number
            )

            LabeledTextField(
                label = "Cel",
                value = uiState.goal,
                onValueChange = viewModel::setGoal,
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 3
            )

            ProfileDropdown(
                label = "Poziom aktywnosci",
                value = uiState.activityLevel,
                expanded = uiState.activityExpanded,
                options = viewModel.activityOptions,
                onExpandedChange = viewModel::setActivityExpanded,
                onOptionSelected = viewModel::setActivityLevel
            )

            Button(
                onClick = viewModel::saveProfile,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Zapisz profil")
            }
        }
    }
}

@Composable
fun ScreenHeader(
    title: String,
    subtitle: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    actionContent: (@Composable RowScope.() -> Unit)? = null
) {
    val context = LocalContext.current
    val calendar = remember(selectedDate) {
        Calendar.getInstance().apply {
            val parser = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            runCatching { parser.parse(selectedDate) }.getOrNull()?.let(::setTime)
        }
    }

    Card(shape = RoundedCornerShape(20.dp), backgroundColor = Color.White, elevation = 4.dp) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = subtitle, color = Color(0xFF475569))
            Spacer(modifier = Modifier.height(14.dp))
            val dateWeight = if (actionContent != null) 0.72f else 1f
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(dateWeight)
                        .height(52.dp)
                        .background(Color(0xFFE0E7FF), RoundedCornerShape(12.dp))
                        .clickable {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val parser = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                    val picked = Calendar.getInstance().apply {
                                        set(year, month, dayOfMonth)
                                    }
                                    onDateSelected(parser.format(picked.time))
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF1D4ED8))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Data: $selectedDate", color = Color(0xFF1E3A8A), fontWeight = FontWeight.Medium)
                }

                if (actionContent != null) {
                    actionContent()
                }
            }
        }
    }
}

@Composable
fun HeaderSquareAction(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(96.dp)
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFDBEAFE),
            contentColor = Color(0xFF1E3A8A)
        ),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
    ) {
        Text(text = label, textAlign = TextAlign.Center)
    }
}

@Composable
fun InfoCard(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color.White,
        elevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = body, color = Color(0xFF475569))
        }
    }
}

@Composable
fun GoalInputCard(
    title: String,
    value: String,
    suffix: String,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color.White,
        elevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0F172A))
            LabeledTextField(
                label = suffix,
                value = value,
                onValueChange = { newValue ->
                    if (newValue.all(Char::isDigit)) onValueChange(newValue)
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number
            )
        }
    }
}

@Composable
fun ProgressDonutCard(
    title: String,
    currentValue: Float,
    goalValue: Float,
    primaryLabel: String,
    secondaryLabel: String
) {
    val progress = if (goalValue > 0f) (currentValue / goalValue).coerceIn(0f, 1f) else 0f
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color.White,
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DonutChart(progress = progress, centerText = "${(progress * 100).roundToInt()}%")
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0F172A))
                Text(text = primaryLabel, color = Color(0xFF1D4ED8), fontWeight = FontWeight.Bold)
                Text(text = secondaryLabel, color = Color(0xFF475569))
            }
        }
    }
}

@Composable
fun DonutChart(progress: Float, centerText: String) {
    Box(
        modifier = Modifier.size(110.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            drawArc(
                color = Color(0xFFE2E8F0),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )
            drawArc(
                color = Color(0xFF1D4ED8),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = stroke
            )
        }
        Text(text = centerText, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
    }
}

@Composable
fun TutorialCard(tutorialVideo: TutorialVideo?, onOpen: () -> Unit) {
    val title = tutorialVideo?.title ?: "Film instruktażowy"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color.White,
        elevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) {
                Text("Otworz podglad")
            }
        }
    }
}

@Composable
fun GalleryPreview(images: List<GalleryImage>, onOpen: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color.White,
        elevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Galeria pozycji", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0F172A))
                Text(
                    text = "Zobacz wszystko",
                    color = Color(0xFF1D4ED8),
                    modifier = Modifier.clickable(onClick = onOpen)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            images.forEach { image ->
                Text(text = "• ${image.title}", color = Color(0xFF475569))
            }
        }
    }
}

@Composable
fun MealSection(
    title: String,
    items: List<MealItemData>,
    expanded: Boolean,
    total: Int,
    onToggle: () -> Unit,
    onNameChange: (Int, String) -> Unit,
    onValueChange: (Int, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color.White,
        elevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0F172A))
                    Text(text = "Suma: $total kcal", color = Color(0xFF475569))
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Zwin" else "Rozwin",
                    tint = Color(0xFF1D4ED8),
                    modifier = Modifier.size(28.dp)
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                items.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        LabeledTextField(
                            label = "Co zjedzono",
                            value = item.name,
                            onValueChange = { onNameChange(index, it) },
                            modifier = Modifier.weight(1f)
                        )
                        LabeledTextField(
                            label = "kcal",
                            value = item.calories,
                            onValueChange = { newValue ->
                                if (newValue.all(Char::isDigit)) {
                                    onValueChange(index, newValue)
                                }
                            },
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.width(104.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        minLines = minLines,
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
fun TimePickerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.clickable { showPicker = true }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color(0xFF0F172A),
                disabledBorderColor = Color(0xFFCBD5E1),
                disabledLabelColor = Color(0xFF64748B),
                disabledTrailingIconColor = Color(0xFF1D4ED8),
                backgroundColor = Color.White
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF1D4ED8)
                )
            }
        )
    }

    if (showPicker) {
        ModernTimePickerDialog(
            initialValue = value,
            label = label,
            onDismiss = { showPicker = false },
            onConfirm = { selectedTime ->
                onValueChange(selectedTime)
                showPicker = false
            }
        )
    }
}

@Composable
fun ModernTimePickerDialog(
    initialValue: String,
    label: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val initialHour = initialValue.substringBefore(":").toIntOrNull() ?: 22
    val initialMinute = initialValue.substringAfter(":", "00").toIntOrNull() ?: 0
    var selectedHour by remember(initialValue) { mutableStateOf(initialHour.coerceIn(0, 23)) }
    var selectedMinute by remember(initialValue) { mutableStateOf(initialMinute.coerceIn(0, 59)) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            backgroundColor = Color.White,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D4ED8)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TimeUnitPicker(
                        title = "Godzina",
                        value = selectedHour,
                        range = 0..23,
                        onValueChange = { selectedHour = it },
                        modifier = Modifier.weight(1f)
                    )
                    TimeUnitPicker(
                        title = "Minuty",
                        value = selectedMinute,
                        range = 0..59,
                        onValueChange = { selectedMinute = it },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Anuluj",
                        color = Color(0xFF64748B),
                        modifier = Modifier
                            .clickable(onClick = onDismiss)
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute))
                        },
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Zapisz")
                    }
                }
            }
        }
    }
}

@Composable
fun TimeUnitPicker(
    title: String,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color(0xFFF8FAFC),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                color = Color(0xFF64748B),
                fontSize = 14.sp
            )
            IconButton(onClick = {
                onValueChange(if (value >= range.last) range.first else value + 1)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "W gore",
                    tint = Color(0xFF1D4ED8)
                )
            }
            Text(
                text = String.format(Locale.getDefault(), "%02d", value),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            IconButton(onClick = {
                onValueChange(if (value <= range.first) range.last else value - 1)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "W dol",
                    tint = Color(0xFF1D4ED8)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileDropdown(
    label: String,
    value: String,
    expanded: Boolean,
    options: List<String>,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit
) {
    Column {
        Text(text = label, color = Color(0xFF475569), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Box {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                elevation = 0.dp,
                backgroundColor = Color.White,
                onClick = { onExpandedChange(!expanded) }
            ) {
                Text(
                    text = if (value.isBlank()) "Wybierz" else value,
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF0F172A)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.fillMaxWidth(0.92f)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(onClick = { onOptionSelected(option) }) {
                        Text(option)
                    }
                }
            }
        }
    }
}

@Composable
fun TutorialVideoDialog(tutorialVideo: TutorialVideo?, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val videoResId = remember(tutorialVideo?.resourceName) {
        tutorialVideo?.resourceName?.let { name ->
            context.resources.getIdentifier(name, "raw", context.packageName)
        } ?: 0
    }
    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animateIn = true
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = tween(durationMillis = 280),
        label = "tutorialVideoAlpha"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0.94f,
        animationSpec = tween(durationMillis = 320),
        label = "tutorialVideoScale"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.88f))
                .graphicsLayer {
                    alpha = animatedAlpha
                    scaleX = animatedScale
                    scaleY = animatedScale
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                if (videoResId != 0) {
                    VideoPlayer(
                        resourceId = videoResId,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Nie znaleziono pliku video: ${tutorialVideo?.resourceName ?: "brak"}",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tutorialVideo?.title ?: "Film instruktażowy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Zamknij", tint = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun PositionsGalleryDialog(images: List<GalleryImage>, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                shape = RoundedCornerShape(22.dp),
                backgroundColor = Color.White
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Galeria pozycji",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color(0xFF0F172A)
                            )
                            Card(
                                modifier = Modifier.clickable(onClick = onDismiss),
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = Color(0xFFE2E8F0),
                                elevation = 0.dp
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Zamknij galerie",
                                    tint = Color(0xFF0F172A),
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }

                    items(images) { image ->
                        GalleryImageRow(image = image)
                    }
                }
            }
        }
    }
}

@Composable
fun VideoPlayer(resourceId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val mediaController = remember(context) { MediaController(context) }
    var currentVideoView by remember { mutableStateOf<VideoView?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            currentVideoView?.stopPlayback()
        }
    }

    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                val uri = Uri.parse("android.resource://${ctx.packageName}/$resourceId")
                setVideoURI(uri)
                mediaController.setAnchorView(this)
                setMediaController(mediaController)
                setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.isLooping = true
                    start()
                }
                currentVideoView = this
            }
        },
        modifier = modifier,
        update = { view ->
            currentVideoView = view
        }
    )
}

@Composable
fun ProfilePhoto(profilePhotoUri: String, onChoosePhoto: () -> Unit) {
    Card(
        modifier = Modifier.size(150.dp),
        shape = CircleShape,
        backgroundColor = Color(0xFFFFF1A8),
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onChoosePhoto),
            contentAlignment = Alignment.Center
        ) {
            if (profilePhotoUri.isBlank()) {
                Image(
                    painter = painterResource(id = R.drawable.profile_photo),
                    contentDescription = "Zdjecie profilowe",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    update = { imageView ->
                        imageView.setImageURI(Uri.parse(profilePhotoUri))
                    }
                )
            }
        }
    }
}

@Composable
fun SavedProfileCard(
    uiState: com.example.wrofit.ui.viewmodel.ProfileUiState,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = Color.White,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfilePhoto(profilePhotoUri = uiState.profilePhotoUri, onChoosePhoto = {})
            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Zmien zdjecie profilowe")
            }
            Text(
                text = if (uiState.fullName.isBlank()) "Profil zapisany" else uiState.fullName,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center
            )
            SavedProfileRow(label = "Plec", value = uiState.gender)
            SavedProfileRow(label = "Waga", value = uiState.weight.takeIf { it.isNotBlank() }?.plus(" kg").orEmpty())
            SavedProfileRow(label = "Wzrost", value = uiState.height.takeIf { it.isNotBlank() }?.plus(" cm").orEmpty())
            SavedProfileRow(label = "Cel", value = uiState.goal)
            SavedProfileRow(label = "Aktywnosc", value = uiState.activityLevel)
            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Edytuj profil")
            }
            Button(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFFEE2E2),
                    contentColor = Color(0xFFB91C1C)
                )
            ) {
                Text("Usun profil")
            }
        }
    }
}

@Composable
fun SavedProfileRow(label: String, value: String) {
    if (value.isBlank()) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color(0xFF64748B))
        Text(text = value, color = Color(0xFF0F172A), fontWeight = FontWeight.Medium)
    }
}

fun formatOneDecimal(value: Double): String {
    return String.format(Locale.getDefault(), "%.1f", value)
}

@Composable
fun GalleryImageRow(image: GalleryImage) {
    val context = LocalContext.current
    val imageRes = remember(image.drawableName) {
        context.resources.getIdentifier(image.drawableName, "drawable", context.packageName)
    }

    Card(shape = RoundedCornerShape(16.dp), elevation = 2.dp) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (imageRes != 0) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = image.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFFE2E8F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Brak obrazka", color = Color(0xFF475569))
                }
            }

            Text(
                text = image.title,
                modifier = Modifier.padding(14.dp),
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0F172A)
            )
        }
    }
}
