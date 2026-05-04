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
        // Zaczynamy standardowo od logiki klasy bazowej Activity.
        super.onCreate(savedInstanceState)
        setContent {
            // setContent uruchamia drzewo Compose.
            // Każdy element UI od tego miejsca będzie budowany przez funkcje @Composable.
            MaterialTheme {
<<<<<<< Updated upstream
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F9FA)) {
=======
                // Surface działa jak główny kontener Material i ustawia bazowe tło.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F7FB)
                ) {
                    // W tym miejscu przekazujemy sterowanie do głównego composable aplikacji.
>>>>>>> Stashed changes
                    WroFitApp()
                }
            }
        }
    }
}

@Composable
fun WroFitApp() {
<<<<<<< Updated upstream
=======
    // Każdy ekran dostaje własny ViewModel odpowiedzialny za stan i logikę.
    // Dzięki temu logika Home, Food, Sleep, Exercise i Profile jest rozdzielona.
    val navViewModel: NavigationViewModel = viewModel()
>>>>>>> Stashed changes
    val homeViewModel: HomeViewModel = viewModel()
    val navigationViewModel: NavigationViewModel = viewModel()
    val foodViewModel: FoodViewModel = viewModel()
    val sleepViewModel: SleepViewModel = viewModel()
    val exerciseViewModel: ExerciseViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
<<<<<<< Updated upstream
=======
    // profileId jest używany do rozdzielania danych wielu profili.
    val activeProfileId = profileViewModel.uiState.profileId

    LaunchedEffect(navViewModel.selectedDate, activeProfileId) {
        // Gdy zmieni się data albo aktywny profil, synchronizujemy wspólny kontekst
        // pomiędzy ekranami pracującymi na danych dziennych.
        val date = navViewModel.selectedDate
        // Najpierw rozsyłamy datę do wszystkich ekranów zależnych od kontekstu dnia.
        homeViewModel.updateSelectedDate(date)
        foodViewModel.setActiveProfile(activeProfileId)
        foodViewModel.setSelectedDate(date)
        sleepViewModel.setActiveProfile(activeProfileId)
        sleepViewModel.setSelectedDate(date)
        exerciseViewModel.setActiveProfile(activeProfileId)
        exerciseViewModel.setSelectedDate(date)
    }
>>>>>>> Stashed changes

    Scaffold(
        bottomBar = {
<<<<<<< Updated upstream
            CustomBottomNavigation(navigationViewModel.selectedTab, navigationViewModel::updateSelectedTab)
=======
            // bottomBar to dedykowane miejsce Scaffolda na element przyklejony do dołu ekranu.
            CustomBottomNavigation(
                selected = navViewModel.selectedTab,
                onSelected = navViewModel::updateSelectedTab
            )
>>>>>>> Stashed changes
        }
    ) { padding ->
        Box(
            modifier = Modifier
<<<<<<< Updated upstream
=======
                .fillMaxSize()
                // Dodajemy bezpieczny odstęp od górnego paska systemowego.
                .statusBarsPadding()
                // Dodajemy też padding wyliczony przez Scaffold.
>>>>>>> Stashed changes
                .padding(padding)
                .safeDrawingPadding()
        ) {
<<<<<<< Updated upstream
            when (navigationViewModel.selectedTab) {
                0 -> HomeScreen(homeViewModel)
                1 -> FoodScreen(foodViewModel)
                2 -> SleepScreen(sleepViewModel)
                3 -> ExerciseScreen(exerciseViewModel)
=======
            // Wybrana zakładka decyduje, który ekran zostanie wyrenderowany.
            when (navViewModel.selectedTab) {
                0 -> HomeScreen(homeViewModel, navViewModel)
                1 -> FoodScreen(foodViewModel, navViewModel)
                2 -> SleepScreen(sleepViewModel, navViewModel)
                3 -> ExerciseScreen(exerciseViewModel, navViewModel)
>>>>>>> Stashed changes
                4 -> ProfileScreen(profileViewModel)
            }
        }
    }
}

@Composable
<<<<<<< Updated upstream
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
        TutorialVideoDialog(viewModel = viewModel, onDismiss = viewModel::hideTutorialVideo)
    }

    if (viewModel.isPositionsGalleryVisible) {
        PositionsGalleryDialog(viewModel = viewModel, onDismiss = viewModel::hidePositionsGallery)
=======
fun CustomBottomNavigation(selected: Int, onSelected: (Int) -> Unit) {
    // Każdy wpis na dole zawiera etykietę, ikonę i numer zakładki.
    val items = listOf(
        Triple("Główna", Icons.Outlined.Home, 0),
        Triple("Jedzenie", Icons.Outlined.ShoppingCart, 1),
        Triple("Sen", Icons.Outlined.DarkMode, 2),
        Triple("Ćwiczenia", Icons.Outlined.FlashOn, 3),
        Triple("Profil", Icons.Outlined.Person, 4)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            // Kolor tła jest taki sam jak tło główne, aby pasek wyglądał spójnie z ekranem.
            .background(Color(0xFFF5F7FB))
            // navigationBarsPadding pilnuje, by pasek nie zachodził na systemową nawigację telefonu.
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFFF0EBEB))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (label, icon, index) ->
                // isSelected wpływa na kolory i tło aktywnego przycisku.
                val isSelected = selected == index

                Column(
                    modifier = Modifier
                        // weight(1f) sprawia, że wszystkie przyciski mają równą szerokość.
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color(0xFFFFFFFF) else Color.Transparent)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        // Kliknięcie nie zmienia UI bezpośrednio.
                        // Zamiast tego zgłaszamy zamiar zmiany do NavigationViewModel.
                        ) { onSelected(index) }
                        // Padding powiększa obszar dotykowy i poprawia czytelność.
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) Color(0xFF222222) else Color(0xFF5B5B5B),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = label,
                        color = if (isSelected) Color(0xFF222222) else Color(0xFF444444),
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel, navViewModel: NavigationViewModel) {
    // LiveData z Room jest obserwowane i zamieniane na stan Compose.
    val galleryImages by viewModel.galleryImages.observeAsState(emptyList())
    val tutorialVideo by viewModel.tutorialVideo.observeAsState()

    // LazyColumn oznacza przewijaną listę pionową.
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Pierwszy element listy to nagłówek ekranu głównego.
            ScreenHeader(
                title = "WroFit",
                subtitle = "Twoje codzienne wsparcie zdrowych nawyków",
                selectedDate = navViewModel.selectedDate,
                onDateSelected = navViewModel::updateSelectedDate,
                showDate = false
            )
        }

        item {
            // Drugi element to karta otwierająca film instruktażowy.
            TutorialCard(
                tutorialVideo = tutorialVideo,
                onOpen = viewModel::showTutorialVideo
            )
        }

        if (galleryImages.isNotEmpty()) {
            item {
                // Jeśli mamy obrazy, pokazujemy tylko krótki podgląd galerii.
                GalleryPreview(
                    images = galleryImages.take(4),
                    onOpen = viewModel::showPositionsGallery
                )
            }
        }
    }

    if (viewModel.isTutorialVideoVisible) {
        // Dialog nie istnieje stale w drzewie UI - pojawia sie tylko po zmianie flagi.
        TutorialVideoDialog(
            tutorialVideo = tutorialVideo,
            onDismiss = viewModel::hideTutorialVideo
        )
    }

    if (viewModel.isPositionsGalleryVisible) {
        // Drugi dialog jest niezależny od filmu i ma własną flagę widoczności.
        PositionsGalleryDialog(
            images = galleryImages,
            onDismiss = viewModel::hidePositionsGallery
        )
>>>>>>> Stashed changes
    }
}

@Composable
<<<<<<< Updated upstream
fun TutorialCard(title: String, buttonText: String, onClick: () -> Unit = {}) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), shape = RoundedCornerShape(12.dp), backgroundColor = Color(0xFFE0E0E0)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontWeight = FontWeight.Medium)
            Button(onClick = onClick, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black), shape = RoundedCornerShape(8.dp)) {
                Text(buttonText, color = Color.White)
=======
fun FoodScreen(viewModel: FoodViewModel, navViewModel: NavigationViewModel) {
    // UI czyta gotowy stan, a obliczenia deleguje do ViewModelu.
    val uiState = viewModel.uiState
    // Suma kalorii jest wyliczana na bieżąco z wpisów formularza.
    val totalCalories = viewModel.overallTotal()
    // Cel kalorii jest parsowany z tekstu do liczby przez ViewModel.
    val calorieGoal = viewModel.calorieGoal()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Nagłówek ekranu Food pokazuje również aktualnie wybraną datę.
            ScreenHeader(
                title = "Kalorie",
                subtitle = "Uzupełniaj posiłki dla wybranego dnia",
                selectedDate = navViewModel.selectedDate,
                onDateSelected = navViewModel::updateSelectedDate
            )
        }

        item {
            // Ta karta pozwala ustawić maksymalny limit kalorii na dany dzień.
            GoalInputCard(
                title = "Maks. kalorii na dzień",
                value = uiState.dailyCalorieGoal,
                suffix = "kcal",
                onValueChange = viewModel::setDailyCalorieGoal
            )
        }

        item {
            // Ta karta wizualizuje postęp względem ustawionego limitu.
            ProgressDonutCard(
                title = "Postęp kalorii",
                currentValue = totalCalories.toFloat(),
                goalValue = calorieGoal.toFloat(),
                primaryLabel = "$totalCalories kcal",
                secondaryLabel = if (calorieGoal > 0) "Cel: $calorieGoal kcal" else "Ustaw maks. kalorii"
            )
        }

        item {
            // Sekcja śniadania dostaje listę pól i callbacki do zmian.
            MealSection(
                title = "Śniadanie",
                items = uiState.breakfastItems,
                expanded = uiState.breakfastExpanded,
                total = viewModel.breakfastTotal(),
                onToggle = viewModel::toggleBreakfast,
                onNameChange = viewModel::setBreakfastName,
                onValueChange = viewModel::setBreakfastCalories
            )
        }

        item {
            // Sekcja obiadu działa identycznie jak śniadanie, ale na innej liście danych.
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
            // Sekcja kolacji domyka cały formularz dnia.
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
            // Na końcu pokazujemy tekstowe podsumowanie całego dnia.
            InfoCard(
                title = "Łączna ilość kalorii w ciągu dnia",
                body = "$totalCalories kcal"
            )
        }
    }
}

@Composable
fun SleepScreen(viewModel: SleepViewModel, navViewModel: NavigationViewModel) {
    val uiState = viewModel.uiState
    // Liczba godzin snu jest liczona dynamicznie na podstawie godzin snu i pobudki.
    val sleptHours = viewModel.sleptHours()
    val sleepGoal = viewModel.sleepGoal()

    // Dla małej, stałej liczby elementów wystarcza zwykła Column z przewijaniem.
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
            // To pole działa identycznie, ale ustawia godzinę pobudki.
            label = "Godzina pobudki",
            value = uiState.wakeTime,
            onValueChange = viewModel::setWakeTime,
            modifier = Modifier.fillMaxWidth()
        )

        GoalInputCard(
            // Cel snu jest liczony w godzinach zamiast w kcal.
            title = "Cel godzinowy snu",
            value = uiState.sleepGoalHours,
            suffix = "godziny",
            onValueChange = viewModel::setSleepGoalHours
        )

        ProgressDonutCard(
            title = "Postęp snu",
            currentValue = sleptHours.toFloat(),
            goalValue = sleepGoal.toFloat(),
            primaryLabel = "${formatOneDecimal(sleptHours)} h",
            secondaryLabel = if (sleepGoal > 0) "Cel: $sleepGoal h" else "Ustaw cel godzinowy"
        )

        LabeledTextField(
            label = "Trudności ze snem",
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
        // Zaznaczone cwiczenia trafiaja na poczatek listy, dzieki czemu latwiej
        // zobaczyc co juz zostalo wykonane danego dnia.
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
                title = "Aktywność",
                subtitle = "Odhacz wykonane ćwiczenia",
                selectedDate = navViewModel.selectedDate,
                onDateSelected = navViewModel::updateSelectedDate,
                actionContent = {
                    // To dodatkowy slot nagłówka na mały przycisk akcji.
                    HeaderSquareAction(
                        label = "Wyczyść",
                        onClick = viewModel::resetCheckedExercises
                    )
                }
            )
        }

        items(orderedExercises) { exercise ->
            // Dla każdej nazwy ćwiczenia powstaje osobna karta z checkboxem.
            Card(shape = RoundedCornerShape(16.dp), elevation = 3.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        // Stan checkboxa wynika bezpośrednio z aktualnego uiState.
                        checked = exercise in uiState.checkedExercises,
                        onCheckedChange = { checked -> viewModel.toggleExercise(exercise, checked) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = exercise, fontSize = 16.sp)
                }
>>>>>>> Stashed changes
            }
        }
    }
}

@Composable
<<<<<<< Updated upstream
fun TutorialVideoDialog(viewModel: HomeViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val tutorialVideo by viewModel.tutorialVideo.observeAsState(initial = null)
    val videoResourceId = remember(tutorialVideo?.resourceName) {
        tutorialVideo?.resourceName?.let { resourceName ->
            context.resources.getIdentifier(resourceName, "raw", context.packageName)
        } ?: 0
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
=======
fun ProfileScreen(viewModel: ProfileViewModel) {
    val uiState = viewModel.uiState
    // Launcher otwiera systemowy selektor plikow i zwraca URI wybranego zdjecia.
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.saveProfilePhotoFromPickerUri(uri)
        }
    }

    // Cały ekran profilu jest przewijany, bo formularz może być dłuższy niż wysokość urządzenia.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (uiState.isProfileSaved) {
            // Po zapisaniu pokazujemy kartę podsumowania zamiast formularza edycji.
            SavedProfileCard(
                uiState = uiState,
                onEdit = viewModel::editProfile,
                onDelete = viewModel::deleteProfile
            )
        } else {
            // Jeśli profil nie jest zapisany, użytkownik widzi formularz wypełnienia danych.
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
                        // Kliknięcie w zdjęcie uruchamia wybór obrazów z systemu.
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
                        text = "Miejsce na zdjęcie profilowe i podstawowe dane użytkownika.",
                        color = Color(0xFF475569),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // Osobny przycisk daje drugą, bardziej oczywistą drogę do dodania zdjęcia.
                    Button(onClick = { photoPicker.launch("image/*") }) {
                        Text("Dodaj zdjęcie profilowe")
                    }
                }
            }

            // Od tego miejsca zaczyna się właściwy formularz danych użytkownika.
            LabeledTextField(
                label = "Imię i nazwisko",
                value = uiState.fullName,
                onValueChange = viewModel::setFullName,
                modifier = Modifier.fillMaxWidth()
            )

            ProfileDropdown(
                label = "Płeć",
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
                label = "Poziom aktywności",
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
    showDate: Boolean = true,
    actionContent: (@Composable RowScope.() -> Unit)? = null
) {
    // LocalContext.current daje dostęp do Androidowego Context wewnątrz Compose.
    val context = LocalContext.current
    val calendar = remember(selectedDate) {
        // remember(selectedDate) tworzy Calendar ustawiony na aktualnie wybraną datę.
        // Dzięki temu DatePicker otwiera się od razu na właściwym dniu.
        Calendar.getInstance().apply {
            val parser = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            runCatching { parser.parse(selectedDate) }.getOrNull()?.let(::setTime)
        }
    }

    Card(shape = RoundedCornerShape(20.dp), backgroundColor = Color.White, elevation = 4.dp) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Tytuł jest wizualnie najważniejszym elementem nagłówka.
            Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(6.dp))
            // Subtitle dopowiada użytkownikowi, do czego służy ekran.
            Text(text = subtitle, color = Color(0xFF475569))
            if (showDate || actionContent != null) {
                // Dolny wiersz nagłówka pojawia się tylko wtedy, gdy mamy datę albo akcję.
                Spacer(modifier = Modifier.height(14.dp))
                val dateWeight = if (actionContent != null) 0.72f else 1f
>>>>>>> Stashed changes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
<<<<<<< Updated upstream
                    Text(
                        tutorialVideo?.title ?: "Film instruktażowy",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onDismiss) {
                        Text("Zamknij", color = Color.White)
=======
                    if (showDate) {
                        Row(
                            modifier = Modifier
                                .weight(dateWeight)
                                .height(52.dp)
                                .background(Color(0xFFE0E7FF), RoundedCornerShape(12.dp))
                                .clickable {
                                    // Androidowy DatePicker zwraca osobno rok, miesiąc i dzień.
                                    // My zamieniamy je od razu na jeden String używany w całej aplikacji.
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            val parser = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                            val picked = Calendar.getInstance().apply {
                                                // Ustawiamy datę wybraną przez użytkownika w oknie dialogowym.
                                                set(year, month, dayOfMonth)
                                            }
                                            // Sformatowaną datę oddajemy do ViewModelu / ekranu wywołującego.
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
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
>>>>>>> Stashed changes
                    }
                }

<<<<<<< Updated upstream
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
                            "Nie znaleziono konfiguracji filmu w bazie albo pliku w zasobach `res/raw`.",
                            color = Color.White
                        )
=======
                    if (actionContent != null) {
                        // actionContent jest slotem na dowolny dodatkowy composable.
                        actionContent()
>>>>>>> Stashed changes
                    }
                }
            }
        }
    }
}

@Composable
<<<<<<< Updated upstream
fun PositionsGalleryDialog(viewModel: HomeViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val galleryItems by viewModel.galleryImages.observeAsState(initial = emptyList())

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
=======
fun HeaderSquareAction(label: String, onClick: () -> Unit) {
    // To mały przycisk akcji, który mieści się obok pola daty w nagłówku.
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
    // Uniwersalna karta tekstowa do prostych komunikatów i podsumowań.
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color.White,
        elevation = 3.dp
>>>>>>> Stashed changes
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F4F4))
                .padding(12.dp)
        ) {
<<<<<<< Updated upstream
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
=======
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0F172A))
            LabeledTextField(
                label = suffix,
                value = value,
                onValueChange = { newValue ->
                    // Dopuszczamy wyłącznie cyfry, bo pole reprezentuje liczbę.
                    if (newValue.all(Char::isDigit)) onValueChange(newValue)
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number
            )
>>>>>>> Stashed changes
        }
    }
}

@Composable
<<<<<<< Updated upstream
fun GalleryImageCard(item: GalleryImage, context: android.content.Context) {
    val drawableId = remember(item.drawableName) {
        context.resources.getIdentifier(item.drawableName, "drawable", context.packageName)
    }

=======
fun ProgressDonutCard(
    title: String,
    currentValue: Float,
    goalValue: Float,
    primaryLabel: String,
    secondaryLabel: String
) {
    // Progres jest ograniczony do zakresu 0..1, zeby wykres nie "wylewal sie"
    // poza pelne 100% nawet gdy wartosc celu zostanie przekroczona.
    val progress = if (goalValue > 0f) (currentValue / goalValue).coerceIn(0f, 1f) else 0f
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
=======
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
            // Stroke określa grubość i wygląd linii obu łuków.
            val stroke = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            // Najpierw rysujemy pełne tło okręgu.
            drawArc(
                color = Color(0xFFE2E8F0),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )
            // Potem nakładamy łuk postępu proporcjonalny do wartości progress.
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
                Text("Otwórz podgląd")
            }
        }
    }
}

@Composable
fun GalleryPreview(images: List<GalleryImage>, onOpen: () -> Unit) {
    // To skrócona wersja galerii pokazywana na ekranie głównym.
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
    // Jedna sekcja reprezentuje jeden typ posiłku: śniadanie, obiad albo kolację.
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
                    // Kliknięcie nagłówka sekcji zwija lub rozwija pola formularza.
                    .clickable(onClick = onToggle),
                horizontalArrangement = Arrangement.SpaceBetween,
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
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
=======
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                items.forEachIndexed { index, item ->
                    // Kazdy posilek ma trzy wiersze, a index wskazuje ktory z nich edytujemy.
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
                                // Pole kalorii akceptuje tylko liczby.
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
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
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
=======
    // To wrapper wokół OutlinedTextField, żeby nie duplikować konfiguracji pól w wielu miejscach.
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
fun SleepScreen(viewModel: SleepViewModel) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    LaunchedEffect(Unit) {
        if (uiState.selectedDate.isBlank()) {
            viewModel.setSelectedDate(dateFormatter.format(calendar.time))
=======
fun TimePickerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // showPicker steruje widocznością własnego dialogu czasu.
    var showPicker by remember { mutableStateOf(false) }

    Box(
        // Kliknięcie w pole otwiera picker zamiast wpisywania tekstu z klawiatury.
        modifier = modifier.clickable { showPicker = true }
    ) {
        OutlinedTextField(
            value = value,
            // Pole jest tylko do odczytu, więc użytkownik nie edytuje go ręcznie.
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
                // Ikona sugeruje, że pole otwiera kontrolkę wyboru czasu.
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF1D4ED8)
                )
            }
        )
    }

    if (showPicker) {
        // Wlasny dialog czasu zwraca gotowy napis HH:mm.
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
    // Pola startowe sa odczytywane z aktualnej wartosci, aby edycja zaczynala sie
    // od tego co widzi uzytkownik na ekranie.
    val initialHour = initialValue.substringBefore(":").toIntOrNull() ?: 22
    val initialMinute = initialValue.substringAfter(":", "00").toIntOrNull() ?: 0
    // coerceIn pilnuje poprawnych zakresów nawet przy uszkodzonym wejściu.
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
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
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
=======
fun ProfileDropdown(
    label: String,
    value: String,
    expanded: Boolean,
    options: List<String>,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit
) {
    // Rozwijane pole jest w pełni kontrolowane przez stan expanded przekazany z zewnątrz.
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
                // Jeśli jeszcze nic nie wybrano, pokazujemy neutralny tekst "Wybierz".
                Text(
                    text = if (value.isBlank()) "Wybierz" else value,
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF0F172A)
                )
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
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
=======
                options.forEach { option ->
                    // Kliknięcie opcji odsyła wybraną wartość z powrotem do ViewModelu.
                    DropdownMenuItem(onClick = { onOptionSelected(option) }) {
>>>>>>> Stashed changes
                        Text(option)
                    }
                }
            }
        }
    }
}

@Composable
<<<<<<< Updated upstream
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
=======
fun TutorialVideoDialog(tutorialVideo: TutorialVideo?, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val videoResId = remember(tutorialVideo?.resourceName) {
        // W bazie mamy tylko tekstową nazwę zasobu.
        // Tutaj zamieniamy ją na prawdziwe ID pliku z folderu res/raw.
        tutorialVideo?.resourceName?.let { name ->
            context.resources.getIdentifier(name, "raw", context.packageName)
        } ?: 0
    }
    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        // Po pojawieniu się dialogu włączamy animację wejścia.
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
        // Wyłączamy domyślną szerokość platformy, aby dialog mógł zająć prawie cały ekran.
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
                    // Jeśli zasób wideo istnieje, delegujemy odtwarzanie do osobnego composable.
                    VideoPlayer(
                        resourceId = videoResId,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Jeśli JSON wskazuje nieistniejący plik, pokazujemy komunikat zamiast crasha.
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Nie znaleziono pliku wideo: ${tutorialVideo?.resourceName ?: "brak"}",
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
                // LazyColumn jest tu wygodna, bo galeria może mieć więcej elementów niż wysokość ekranu.
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
                                    contentDescription = "Zamknij galerię",
                                    tint = Color(0xFF0F172A),
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }

                    items(images) { image ->
                        // Każdy rekord galerii renderujemy jednym wspólnym komponentem.
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
    // MediaController dodaje klasyczne kontrolki przewijania i pauzy.
    val mediaController = remember(context) { MediaController(context) }
    var currentVideoView by remember { mutableStateOf<VideoView?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            // Sprzatamy odtwarzacz przy zamykaniu dialogu, aby nie gral dalej w tle.
            currentVideoView?.stopPlayback()
        }
    }

    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                // AndroidView pozwala osadzic klasyczny widok Androida wewnatrz Compose.
                val uri = Uri.parse("android.resource://${ctx.packageName}/$resourceId")
                // URI wskazuje film zapisany w zasobach aplikacji.
                setVideoURI(uri)
                mediaController.setAnchorView(this)
                setMediaController(mediaController)
                setOnPreparedListener { mediaPlayer ->
                    // Gdy odtwarzacz jest gotowy, startujemy i włączamy zapętlenie.
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
    // Ten komponent odpowiada wyłącznie za pokazanie zdjęcia i reakcję na kliknięcie.
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
                // Brak wybranego zdjecia oznacza wyswietlenie domyslnego obrazka z drawable.
                Image(
                    painter = painterResource(id = R.drawable.profile_photo),
                    contentDescription = "Zdjęcie profilowe",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Gdy mamy URI z galerii systemowej, uzywamy klasycznego ImageView do jego wyswietlenia.
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
                        try {
                            imageView.setImageURI(Uri.parse(profilePhotoUri))
                        } catch (_: SecurityException) {
                            imageView.setImageResource(R.drawable.profile_photo)
                        } catch (_: IllegalArgumentException) {
                            imageView.setImageResource(R.drawable.profile_photo)
                        }
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
    // To widok "tylko do odczytu" pokazywany po zapisaniu profilu.
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
                Text("Zmień zdjęcie profilowe")
            }
            Text(
                text = if (uiState.fullName.isBlank()) "Profil zapisany" else uiState.fullName,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center
            )
            SavedProfileRow(label = "Płeć", value = uiState.gender)
            // takeIf(...).plus(...) pozwala dodać jednostkę tylko wtedy, gdy pole nie jest puste.
            SavedProfileRow(label = "Waga", value = uiState.weight.takeIf { it.isNotBlank() }?.plus(" kg").orEmpty())
            SavedProfileRow(label = "Wzrost", value = uiState.height.takeIf { it.isNotBlank() }?.plus(" cm").orEmpty())
            SavedProfileRow(label = "Cel", value = uiState.goal)
            SavedProfileRow(label = "Aktywność", value = uiState.activityLevel)
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
>>>>>>> Stashed changes
                )
            }
        }
    }
}

@Composable
<<<<<<< Updated upstream
fun FoodRow(entry: FoodEntry, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), elevation = 1.dp) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.mealName, fontWeight = FontWeight.Bold)
                Text("${entry.mealType} • ${entry.date}", fontSize = 12.sp, color = Color.Gray)
=======
fun SavedProfileRow(label: String, value: String) {
    // Pustych wierszy nie pokazujemy, żeby karta nie miała martwego miejsca.
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
    // Ujednolicamy format wyświetlania wartości typu 7.5 h.
    return String.format(Locale.getDefault(), "%.1f", value)
}

@Composable
fun GalleryImageRow(image: GalleryImage) {
    val context = LocalContext.current
    val imageRes = remember(image.drawableName) {
        // Analogicznie jak dla wideo: tekstowa nazwa zasobu jest zamieniana na ID drawable.
        context.resources.getIdentifier(image.drawableName, "drawable", context.packageName)
    }

    Card(shape = RoundedCornerShape(16.dp), elevation = 2.dp) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (imageRes != 0) {
                // Jeśli zasób istnieje, pokazujemy prawdziwy obraz z drawable.
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = image.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Gdy zasobu nie ma, pokazujemy łagodny placeholder zamiast błędu.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFFE2E8F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Brak obrazka", color = Color(0xFF475569))
                }
>>>>>>> Stashed changes
            }
            Text("${entry.calories.toInt()} kcal", fontWeight = FontWeight.Bold)
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
        }
    }
}
